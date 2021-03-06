/*
 * Copyright (c) 2018 - 2019 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.nalukit.gwtbootstarternalu.shared.model;

import java.util.ArrayList;
import java.util.List;

public class NaluGeneraterParms {

  private MavenModule mavenSettings;

  private String groupId;

  private String artefactId;

  private String gwtVersion;

  private boolean applicationLoader;

  private boolean debug;

  private boolean errorScreen;

  private boolean loginScreen;

  private boolean hashUrl;

  private WidgetLibrary widgetLibrary;

  private List<ControllerData> controllers;

  public NaluGeneraterParms() {
    this.controllers = new ArrayList<>();
  }

  public MavenModule getMavenSettings() {
    return mavenSettings;
  }

  public void setMavenSettings(MavenModule mavenSettings) {
    this.mavenSettings = mavenSettings;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getArtefactId() {
    return artefactId;
  }

  public void setArtefactId(String artefactId) {
    this.artefactId = artefactId;
  }

  public String getGwtVersion() {
    return gwtVersion;
  }

  public void setGwtVersion(String gwtVersion) {
    this.gwtVersion = gwtVersion;
  }

  public boolean isApplicationLoader() {
    return applicationLoader;
  }

  public void setApplicationLoader(boolean applicationLoader) {
    this.applicationLoader = applicationLoader;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public List<ControllerData> getControllers() {
    return controllers;
  }

  public void setControllers(List<ControllerData> controllers) {
    this.controllers = controllers;
  }

  public WidgetLibrary getWidgetLibrary() {
    return widgetLibrary;
  }

  public void setWidgetLibrary(WidgetLibrary widgetLibrary) {
    this.widgetLibrary = widgetLibrary;
  }

  public boolean hasErrorScreen() {
    return errorScreen;
  }

  public void setErrorScreen(boolean errorScreen) {
    this.errorScreen = errorScreen;
  }

  public boolean hasLoginScreen() {
    return loginScreen;
  }

  public void setLoginScreen(boolean loginScreen) {
    this.loginScreen = loginScreen;
  }

  public boolean hasHashUrl() {
    return hashUrl;
  }

  public void setHashUrl(boolean hashUrl) {
    this.hashUrl = hashUrl;
  }

  public boolean hasNavigationConfirmation() {
    return this.controllers.stream()
                           .anyMatch(ControllerData::isConfirmation);
  }

}
