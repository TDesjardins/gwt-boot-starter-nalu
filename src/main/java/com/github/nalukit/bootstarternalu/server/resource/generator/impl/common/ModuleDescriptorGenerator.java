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

package com.github.nalukit.bootstarternalu.server.resource.generator.impl.common;

import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorUtils;
import com.github.nalukit.gwtbootstarternalu.shared.model.GeneratorException;
import com.github.nalukit.gwtbootstarternalu.shared.model.MavenModule;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ModuleDescriptorGenerator {

  private NaluGeneraterParms naluGeneraterParms;

  private String projectFolder;

  private ModuleDescriptorGenerator(Builder builder) {
    super();

    this.naluGeneraterParms = builder.naluGeneraterParms;
    this.projectFolder = builder.projectFolder;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
      throws GeneratorException {

    Configuration freeMarkerConfiguration = new Configuration();

    freeMarkerConfiguration.setClassForTemplateLoading(ModuleDescriptorGenerator.class,
                                                       "/templates/xml");
    freeMarkerConfiguration.setDefaultEncoding("UTF-8");

    Template template;
    try {
      template = freeMarkerConfiguration.getTemplate("ModuleDescriptor.ftl");
    } catch (IOException e) {
      throw new GeneratorException("Unable to get >>readme.ftl<< -> exception: " + e.getMessage());
    }

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("widgetLibrary",
                     this.naluGeneraterParms.getWidgetLibrary()
                                            .toString());
    templateData.put("artifactIdFirstUpperCase",
                     GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()));
    templateData.put("artifactIdLowerCase",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()
                                                                              .toLowerCase()));
    templateData.put("artifactId",
                     GeneratorUtils.removeBadChracters(this.naluGeneraterParms.getArtefactId()));
    templateData.put("groupId",
                     this.naluGeneraterParms.getGroupId());
    templateData.put("mavenProjectType",
                     MavenModule.MULTI_MAVEN_MODULE.equals(this.naluGeneraterParms.getMavenSettings()) ? "MultiMavenModule" : "SingleMavenModule");
    String pathToModuleDescriptor = this.projectFolder + File.separator + "src" + File.separator + "main";
    File folderModuleDescriptor = new File(pathToModuleDescriptor);
    if (!folderModuleDescriptor.exists()) {
      folderModuleDescriptor.mkdirs();
    }
    try (StringWriter out = new StringWriter()) {
      template.process(templateData,
                       out);
      Files.write(Paths.get(folderModuleDescriptor + File.separator + "module.gwt.xml"),
                  out.toString()
                     .getBytes());
      out.flush();
    } catch (IOException e) {
      throw new GeneratorException("Unable to write generated file: >>" + folderModuleDescriptor + File.separator + "module.gwt.xml" + "<< -> exception: " + e.getMessage());
    } catch (TemplateException e) {
      throw new GeneratorException("Unable to write generated file: >>" + folderModuleDescriptor + File.separator + "module.gwt.xml" + "<< -> exception: " + e.getMessage());
    }
  }

  public static class Builder {

    NaluGeneraterParms naluGeneraterParms;

    String projectFolder;

    public Builder naluGeneraterParms(NaluGeneraterParms naluGeneraterParms) {
      this.naluGeneraterParms = naluGeneraterParms;
      return this;
    }

    public Builder projectFolder(String projectFolder) {
      this.projectFolder = projectFolder;
      return this;
    }

    public ModuleDescriptorGenerator build() {
      return new ModuleDescriptorGenerator(this);
    }

  }

}
