/*******************************************************************************
 * Copyright (c) 2013 Cloud Bees, Inc.
 * All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Cloud Bees, Inc. - initial API and implementation 
 *******************************************************************************/
package com.cloudbees.eclipse.dev.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import com.cloudbees.eclipse.core.jenkins.api.JenkinsBuild;
import com.cloudbees.eclipse.core.jenkins.api.JenkinsJobsResponse;
import com.cloudbees.eclipse.dev.ui.CBDEVImages;
import com.cloudbees.eclipse.dev.ui.CloudBeesDevUiPlugin;

public class OpenBuildAction extends Action {

  private Object build;

  public OpenBuildAction(final boolean last) {
    super(last ? "Open last build details" : "Open build details", Action.AS_PUSH_BUTTON | SWT.NO_FOCUS); //TODO i18n
    setToolTipText(last ? "Open last build details of this job" : "Open build details"); //TODO i18n
    setImageDescriptor(CloudBeesDevUiPlugin.getImageDescription(CBDEVImages.IMG_BUILD_DETAILS));
    super.setEnabled(false);
  }

  @Override
  public void setEnabled(final boolean enable) {
    // ignore
  }

  public void setBuild(final Object build) {
    this.build = build;
    super.setEnabled(this.build != null);
  }

  @Override
  public boolean isEnabled() {
    return this.build != null;
  }

  @Override
  public void runWithEvent(final Event event) {
    if (this.build instanceof JenkinsJobsResponse.Job) {
      CloudBeesDevUiPlugin.getDefault().showBuildForJob(((JenkinsJobsResponse.Job) this.build));
    } else if (this.build instanceof JenkinsBuild) {
      CloudBeesDevUiPlugin.getDefault().showBuild(((JenkinsBuild) this.build));
    }
  }

}
