package com.cloudbees.eclipse.dev.ui.actions;

import java.util.concurrent.CancellationException;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.cloudbees.eclipse.core.jenkins.api.JenkinsBuildDetailsResponse;
import com.cloudbees.eclipse.core.jenkins.api.JenkinsBuildDetailsResponse.Artifact;
import com.cloudbees.eclipse.dev.ui.CBImages;
import com.cloudbees.eclipse.dev.ui.CloudBeesDevUiPlugin;
import com.cloudbees.eclipse.dev.ui.views.build.ArtifactsClickListener;

public class DeployWarAction extends Action {

  protected JenkinsBuildDetailsResponse build;

  public DeployWarAction() {
    super("Deploy war", Action.AS_PUSH_BUTTON | SWT.NO_FOCUS); //$NON-NLS-1$
    setToolTipText("Deploy war to specific RUN@cloud application"); //TODO i18n
    setImageDescriptor(CloudBeesDevUiPlugin.getImageDescription(CBImages.IMG_DEPLOY));
    super.setEnabled(false);
  }

  public void setBuild(final JenkinsBuildDetailsResponse build) {
    this.build = build;
    super.setEnabled(isBuildDeployable());
  }

  private boolean isBuildDeployable() {
    if (this.build == null || this.build.artifacts == null) {
      return false;
    }
    for (Artifact art : this.build.artifacts) {
      if (art.relativePath.endsWith(".war")) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void setEnabled(final boolean enable) {
    // ignore
  }

  @Override
  public boolean isEnabled() {
    return isBuildDeployable();
  }

  @Override
  public void run() {
    try {
      if (!isBuildDeployable()) {
        return;
      }

      ArtifactsClickListener.deployWar(this.build, null);
    } catch (CancellationException e) {
      // cancelled by user
    }
  }

}
