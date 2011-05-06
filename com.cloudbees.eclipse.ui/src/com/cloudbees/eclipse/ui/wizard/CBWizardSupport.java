package com.cloudbees.eclipse.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.cloudbees.eclipse.core.CloudBeesException;
import com.cloudbees.eclipse.core.JenkinsService;
import com.cloudbees.eclipse.core.domain.JenkinsInstance;
import com.cloudbees.eclipse.core.forge.api.ForgeInstance;
import com.cloudbees.eclipse.ui.CloudBeesUIPlugin;

public class CBWizardSupport {

  public static JenkinsInstance[] getJenkinsInstances(final IWizardContainer container) throws Exception {
    JenkinsInstance[] result = null;

    final List<JenkinsInstance> instances = new ArrayList<JenkinsInstance>();
    final Failure<CloudBeesException> failiure = new Failure<CloudBeesException>();

    IRunnableWithProgress operation = new IRunnableWithProgress() {

      public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
          monitor.beginTask("Fetching Jenkins instances", 0);
          CloudBeesUIPlugin plugin = CloudBeesUIPlugin.getDefault();
          monitor.subTask("loading local Jenkins instances...");
          List<JenkinsInstance> manualInstances = plugin.loadManualJenkinsInstances();
          monitor.subTask("loading DEV@cloud Jenkins instances...");
          List<JenkinsInstance> cloudInstances = plugin.loadDevAtCloudInstances(monitor);
          instances.addAll(manualInstances);
          instances.addAll(cloudInstances);
        } catch (CloudBeesException e) {
          failiure.cause = e;
        }
      }
    };

    try {
      container.run(true, false, operation);
      result = new JenkinsInstance[instances.size()];
      instances.toArray(result);
    } catch (Exception e) {
      if (failiure.cause != null) {
        throw failiure.cause;
      }
      throw e;
    }

    return result;
  }

  public static ForgeInstance[] getRepos(final IWizardContainer container) throws Exception {
    return getRepos(container, null);
  }

  public static ForgeInstance[] getRepos(final IWizardContainer container, final ForgeInstance.TYPE type)
      throws Exception {
    final List<ForgeInstance> repos = new ArrayList<ForgeInstance>();
    final Failure<CloudBeesException> failiure = new Failure<CloudBeesException>();

    IRunnableWithProgress operation = new IRunnableWithProgress() {

      public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
          monitor.beginTask("Fetching forge repositories", 0);
          List<ForgeInstance> forgeRepos = CloudBeesUIPlugin.getDefault().getForgeRepos(monitor);
          if (type == null) {
            repos.addAll(forgeRepos);
          } else {
            for (ForgeInstance r : forgeRepos) {
              if (r.type.equals(type)) {
                repos.add(r);
              }
            }
          }
        } catch (CloudBeesException e) {
          failiure.cause = e;
        }
      }
    };

    try {
      container.run(true, false, operation);
      return repos.toArray(new ForgeInstance[repos.size()]);
    } catch (Exception e) {
      if (failiure.cause != null) {
        throw failiure.cause;
      }
      throw e;
    }
  }

  public static IStructuredSelection getCurrentSelection() {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (window != null) {
      ISelection selection = window.getSelectionService().getSelection();
      if (selection instanceof IStructuredSelection) {
        return (IStructuredSelection) selection;
      }
    }
    return StructuredSelection.EMPTY;
  }

  public static void makeJenkinsJob(final String configXML, final JenkinsService jenkinsService, final String jobName,
      final IWizardContainer container) throws Exception {

    final Failure<CloudBeesException> failiure = new Failure<CloudBeesException>();

    IRunnableWithProgress operation = new IRunnableWithProgress() {

      public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
          monitor.beginTask("Creating new Jenkins job...", 0);
          jenkinsService.createJenkinsJob(jobName, configXML, monitor);
        } catch (CloudBeesException e) {
          failiure.cause = e;
        }
      }

    };

    try {
      container.run(true, false, operation);
    } catch (Exception e) {
      if (failiure.cause != null) {
        throw failiure.cause;
      }
      throw e;
    }

  }

}
