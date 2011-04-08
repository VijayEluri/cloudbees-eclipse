package com.cloudbees.eclipse.dev.ui;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.cloudbees.eclipse.core.CloudBeesException;
import com.cloudbees.eclipse.core.JenkinsChangeListener;
import com.cloudbees.eclipse.core.JenkinsService;
import com.cloudbees.eclipse.core.jenkins.api.JenkinsJobsResponse;
import com.cloudbees.eclipse.core.jenkins.api.JenkinsJobsResponse.Job;
import com.cloudbees.eclipse.dev.ui.views.build.BuildEditorInput;
import com.cloudbees.eclipse.dev.ui.views.build.BuildPart;
import com.cloudbees.eclipse.dev.ui.views.jobs.JobsView;
import com.cloudbees.eclipse.ui.CloudBeesUIPlugin;
import com.cloudbees.eclipse.ui.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class CloudBeesDevUiPlugin extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "com.cloudbees.eclipse.dev.ui"; //$NON-NLS-1$

  // The shared instance
  private static CloudBeesDevUiPlugin plugin;

  /**
   * The constructor
   */
  public CloudBeesDevUiPlugin() {
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(final BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static CloudBeesDevUiPlugin getDefault() {
    return plugin;
  }

  @Override
  protected void initializeImageRegistry(final ImageRegistry reg) {
    super.initializeImageRegistry(reg);

    reg.put(CBImages.IMG_CONSOLE, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/monitor_obj.png")));
    reg.put(CBImages.IMG_REFRESH, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/refresh.png")));

    reg.put(CBImages.IMG_BROWSER,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/internal_browser.gif")));

    reg.put(CBImages.IMG_RUN, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/lrun_obj.png")));

    reg.put(CBImages.IMG_FOLDER_HOSTED,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/16x16/cb_folder_run.png")));
    reg.put(CBImages.IMG_FOLDER_LOCAL,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/16x16/cb_folder_run.png")));
    reg.put(CBImages.IMG_INSTANCE, ImageDescriptor.createFromURL(getBundle().getResource("/icons/16x16/jenkins.png")));

    reg.put(CBImages.IMG_VIEW,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/16x16/cb_view_dots_big.png")));
    //reg.put(CBImages.IMG_VIEW, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/det_pane_hide.gif")));

    reg.put(CBImages.IMG_FILE, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/file_obj.gif")));

    reg.put(CBImages.IMG_FILE_ADDED, ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/add_stat.gif")));
    reg.put(CBImages.IMG_FILE_MODIFIED,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/mod_stat.gif")));
    reg.put(CBImages.IMG_FILE_DELETED,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/epl/del_stat.gif")));

    reg.put(CBImages.IMG_COLOR_16_GREY,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/grey.gif")));

    reg.put(CBImages.IMG_COLOR_16_RED,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/red.gif")));

    reg.put(CBImages.IMG_COLOR_16_BLUE,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/blue.gif")));

    reg.put(CBImages.IMG_COLOR_24_RED,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/red.gif")));

    reg.put(CBImages.IMG_COLOR_24_BLUE,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/blue.gif")));

    // HEALTH 16px
    reg.put(CBImages.IMG_HEALTH_16_00_to_19,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/health-00to19.gif")));
    reg.put(CBImages.IMG_HEALTH_16_20_to_39,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/health-20to39.gif")));
    reg.put(CBImages.IMG_HEALTH_16_40_to_59,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/health-40to59.gif")));
    reg.put(CBImages.IMG_HEALTH_16_60_to_79,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/health-60to79.gif")));
    reg.put(CBImages.IMG_HEALTH_16_80PLUS,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/16x16/health-80plus.gif")));

    // HEALTH 24px
    reg.put(CBImages.IMG_HEALTH_24_00_to_19,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/health-00to19.gif")));
    reg.put(CBImages.IMG_HEALTH_24_20_to_39,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/health-20to39.gif")));
    reg.put(CBImages.IMG_HEALTH_24_40_to_59,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/health-40to59.gif")));
    reg.put(CBImages.IMG_HEALTH_24_60_to_79,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/health-60to79.gif")));
    reg.put(CBImages.IMG_HEALTH_24_80PLUS,
        ImageDescriptor.createFromURL(getBundle().getResource("/icons/jenkins-icons/24x24/health-80plus.gif")));

  }

  public static Image getImage(final String imgKey) {
    return CloudBeesDevUiPlugin.getDefault().getImageRegistry().get(imgKey);
  }

  public static ImageDescriptor getImageDescription(final String imgKey) {
    return CloudBeesDevUiPlugin.getDefault().getImageRegistry().getDescriptor(imgKey);
  }

  public void showJobs(final String viewUrl, final boolean userAction) throws CloudBeesException {
    // CloudBeesUIPlugin.getDefault().getLogger().info("Show jobs: " + viewUrl);
    System.out.println("Show jobs: " + viewUrl);

    if (viewUrl == null) {
      return; // no info
    }

    org.eclipse.core.runtime.jobs.Job job = new org.eclipse.core.runtime.jobs.Job("Loading Jenkins jobs") {
      @Override
      protected IStatus run(final IProgressMonitor monitor) {
        if (!CloudBeesUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ENABLE_JAAS)) {
          return Status.CANCEL_STATUS;
        }

        try {
          PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            public void run() {
              try {
                CloudBeesUIPlugin
                .getActiveWindow()
                .getActivePage()
                .showView(
                    JobsView.ID,
                    Long.toString(CloudBeesUIPlugin.getDefault().getJenkinsServiceForUrl(viewUrl).getUrl()
                        .hashCode()),
                        userAction ? IWorkbenchPage.VIEW_ACTIVATE : IWorkbenchPage.VIEW_CREATE);
              } catch (PartInitException e) {
                CloudBeesUIPlugin.getDefault().showError("Failed to show Jobs view", e);
              }
            }
          });

          if (monitor.isCanceled()) {
            throw new OperationCanceledException();
          }

          JenkinsJobsResponse jobs = CloudBeesUIPlugin.getDefault().getJenkinsServiceForUrl(viewUrl)
          .getJobs(viewUrl, monitor);

          if (monitor.isCanceled()) {
            throw new OperationCanceledException();
          }

          Iterator<JenkinsChangeListener> iterator = CloudBeesUIPlugin.getDefault().getJenkinsChangeListeners()
          .iterator();
          while (iterator.hasNext()) {
            JenkinsChangeListener listener = iterator.next();
            listener.activeJobViewChanged(jobs);
          }

          return Status.OK_STATUS;
        } catch (CloudBeesException e) {
          CloudBeesUIPlugin.getDefault().getLogger().error(e);
          return new Status(Status.ERROR, PLUGIN_ID, 0, e.getLocalizedMessage(), e.getCause());
        }
      }
    };

    job.setUser(userAction);
    if (CloudBeesUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ENABLE_JAAS)) {
      job.schedule();
    }
  }

  public void showBuildForJob(final Job el) {
    if (el == null) {
      return;
    }
    // Look up the service
    Iterator<JenkinsService> it = CloudBeesUIPlugin.getDefault().getAllJenkinsServices().iterator();
    while (it.hasNext()) {
      JenkinsService service = it.next();
      if (el.url.startsWith(service.getUrl())) {

        try {
          //JobDetailsForm.ID, Utils.toB64(jobUrl), IWorkbenchPage.VIEW_ACTIVATE
          // IEditorDescriptor descr = PlatformUI.getWorkbench().getEditorRegistry().findEditor(JobDetailsForm.ID);

          CloudBeesUIPlugin.getActiveWindow().getActivePage().openEditor(new BuildEditorInput(el), BuildPart.ID);

        } catch (PartInitException e) {
          CloudBeesUIPlugin.getDefault().getLogger().error(e);
        }
        return;
      }
    }
  }

}
