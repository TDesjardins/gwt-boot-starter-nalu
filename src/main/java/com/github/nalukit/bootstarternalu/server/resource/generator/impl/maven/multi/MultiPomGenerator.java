/*
 * Copyright (c) 2018 - 2019 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.github.nalukit.bootstarternalu.server.resource.generator.impl.maven.multi;

import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorUtils;
import com.github.nalukit.gwtbootstarternalu.shared.model.GeneratorException;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MultiPomGenerator {

  private static final Logger logger = LoggerFactory.getLogger(MultiPomGenerator.class);

  private NaluGeneraterParms naluGeneraterParms;

  private String projectFolder;

  private String projectFolderClient;

  private String projectFolderShared;

  private String projectFolderServer;

  private MultiPomGenerator(Builder builder) {
    super();

    this.naluGeneraterParms = builder.naluGeneraterParms;
    this.projectFolder = builder.projectFolder;
    this.projectFolderClient = builder.projectFolderClient;
    this.projectFolderShared = builder.projectFolderShared;
    this.projectFolderServer = builder.projectFolderServer;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
      throws GeneratorException {
    Configuration freeMarkerConfiguration = new Configuration();
    freeMarkerConfiguration.setClassForTemplateLoading(MultiPomGenerator.class,
                                                       "/templates/pom");
    freeMarkerConfiguration.setDefaultEncoding("UTF-8");

    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< start generating pom");
    this.generateProjectPom(freeMarkerConfiguration);
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating pom successfully finished");

    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating client pom");
    this.generateProjectClientPom(freeMarkerConfiguration);
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating client pom sucessfully finished");
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating shared pom");
    this.generateProjectSharedPom(freeMarkerConfiguration);
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating shared pom sucessfully finished");
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating server pom");
    this.generateProjectServerPom(freeMarkerConfiguration);
    logger.debug(">>" + naluGeneraterParms.getArtefactId() + "<< generating server pom sucessfully finished");
  }

  private void generateProjectPom(Configuration freeMarkerConfiguration)
      throws GeneratorException {
    Template template;
    try {
      template = freeMarkerConfiguration.getTemplate("MultiModule.ftl");
    } catch (IOException e) {
      throw new GeneratorException("Unable to get >>MultiModule.ftl<< -> exception: " + e.getMessage());
    }

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("artifactId",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()));
    templateData.put("groupId",
                     this.naluGeneraterParms.getGroupId());
    templateData.put("projectBuildDirectory",
                     "${project.build.directory}");

    try (StringWriter out = new StringWriter()) {
      template.process(templateData,
                       out);
      Files.write(Paths.get(new File(this.projectFolder) + File.separator + "pom.xml"),
                  out.toString()
                     .getBytes());
      out.flush();
    } catch (IOException | TemplateException e) {
      throw new GeneratorException("Unable to write generated file: >>" + this.projectFolder + File.separator + "pom.xml<< -> exception: " + e.getMessage());
    }
  }

  private void generateProjectServerPom(Configuration freeMarkerConfiguration)
      throws GeneratorException {
    Template template;
    try {
      template = freeMarkerConfiguration.getTemplate("MultiModuleServer.ftl");
    } catch (IOException e) {
      throw new GeneratorException("Unable to get >>MultiModuleServer.ftl<< -> exception: " + e.getMessage());
    }

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("basedir",
                     "${basedir}");
    templateData.put("groupId",
                     this.naluGeneraterParms.getGroupId());
    templateData.put("artifactId",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()));
    templateData.put("projectVersion",
                     "${project.version}");

    try (StringWriter out = new StringWriter()) {
      template.process(templateData,
                       out);
      Files.write(Paths.get(new File(this.projectFolderServer) + File.separator + "pom.xml"),
                  out.toString()
                     .getBytes());
      out.flush();
    } catch (IOException | TemplateException e) {
      throw new GeneratorException("Unable to write generated file: >>" + this.projectFolderServer + File.separator + "pom.xml<< -> exception: " + e.getMessage());
    }
  }

  private void generateProjectSharedPom(Configuration freeMarkerConfiguration)
      throws GeneratorException {
    Template template;
    try {
      template = freeMarkerConfiguration.getTemplate("MultiModuleShared.ftl");
    } catch (IOException e) {
      throw new GeneratorException("Unable to get >>MultiModuleShared.ftl<< -> exception: " + e.getMessage());
    }

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("artifactId",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()));
    templateData.put("groupId",
                     this.naluGeneraterParms.getGroupId());

    try (StringWriter out = new StringWriter()) {
      template.process(templateData,
                       out);
      Files.write(Paths.get(new File(this.projectFolderShared) + File.separator + "pom.xml"),
                  out.toString()
                     .getBytes());
      out.flush();
    } catch (IOException | TemplateException e) {
      throw new GeneratorException("Unable to write generated file: >>" + this.projectFolderShared + File.separator + "pom.xml<< -> exception: " + e.getMessage());
    }
  }

  private void generateProjectClientPom(Configuration freeMarkerConfiguration)
      throws GeneratorException {
    Template template;
    try {
      template = freeMarkerConfiguration.getTemplate("MultiModuleClient.ftl");
    } catch (IOException e) {
      throw new GeneratorException("Unable to get >>MultiModuleClient.ftl<< -> exception: " + e.getMessage());
    }

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("widgetLibrary",
                     this.naluGeneraterParms.getWidgetLibrary()
                                            .toString());
    templateData.put("basedir",
                     "${basedir}");
    templateData.put("groupId",
                     this.naluGeneraterParms.getGroupId());
    templateData.put("artifactId",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()));
    templateData.put("artifactIdLowerCase",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId())
                                   .toLowerCase());
    templateData.put("naluVersion",
                     "${nalu.version}");
    templateData.put("dominoVersion",
                     "${domino.version}");
    templateData.put("elementoVersion",
                     "${elemento.version}");
    templateData.put("gxtVersion",
                     "${gxt.version}");
    templateData.put("projectGroupId",
                     "${project.groupId}");
    templateData.put("projectVersion",
                     "${project.version}");

    try (StringWriter out = new StringWriter()) {
      template.process(templateData,
                       out);
      Files.write(Paths.get(new File(this.projectFolderClient) + File.separator + "pom.xml"),
                  out.toString()
                     .getBytes());
      out.flush();
    } catch (IOException | TemplateException e) {
      throw new GeneratorException("Unable to write generated file: >>" + this.projectFolderClient + File.separator + "pom.xml<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    NaluGeneraterParms naluGeneraterParms;

    String projectFolder;

    String projectFolderClient;

    String projectFolderShared;

    String projectFolderServer;

    public Builder naluGeneraterParms(NaluGeneraterParms naluGeneraterParms) {
      this.naluGeneraterParms = naluGeneraterParms;
      return this;
    }

    public Builder projectFolder(String projectFolder) {
      this.projectFolder = projectFolder;
      return this;
    }

    public Builder projectFolderClient(String projectFolderClient) {
      this.projectFolderClient = projectFolderClient;
      return this;
    }

    public Builder projectFolderShared(String projectFolderShared) {
      this.projectFolderShared = projectFolderShared;
      return this;
    }

    public Builder projectFolderServer(String projectFolderServer) {
      this.projectFolderServer = projectFolderServer;
      return this;
    }

    public MultiPomGenerator build() {
      return new MultiPomGenerator(this);
    }

  }

}
