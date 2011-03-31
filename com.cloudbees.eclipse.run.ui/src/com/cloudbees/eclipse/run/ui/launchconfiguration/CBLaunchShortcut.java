package com.cloudbees.eclipse.run.ui.launchconfiguration;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import com.cloudbees.eclipse.run.core.util.CBRunUtil;
import com.cloudbees.eclipse.run.ui.CBRunUiActivator;

public class CBLaunchShortcut implements ILaunchShortcut {

  public void launch(ISelection selection, String mode) {
    if (selection instanceof IStructuredSelection) {
      
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      
      String name = null;
      Object element = structuredSelection.getFirstElement();
      
      if(element instanceof IProject) {
        name = ((IProject) element).getName();
      } else if (element instanceof IJavaProject) {
        name = ((IJavaProject) element).getProject().getName();
      }
      
      try {
        List<ILaunchConfiguration> launchConfigurations = CBRunUtil.getOrCreateCloudBeesLaunchConfigurations(name);
        DebugUITools.launch(launchConfigurations.get(launchConfigurations.size() - 1), mode);
      } catch (CoreException e) {
        CBRunUiActivator.logError(e);
      }
      
    }
  }

  public void launch(IEditorPart editor, String mode) {
    // not currently supported
  }

}