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

package com.github.nalukit.bootstarternalu.server.resource.generator.impl;

import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorConstants;
import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorUtils;
import com.github.nalukit.gwtbootstarternalu.shared.model.GeneratorException;
import com.github.nalukit.nalu.client.component.AbstractComponent;
import com.github.nalukit.nalu.client.component.AbstractComponentController;
import com.github.nalukit.nalu.client.component.IsComponent;
import com.github.nalukit.nalu.client.component.annotation.Controller;
import com.squareup.javapoet.*;
import org.gwtproject.event.shared.HandlerRegistration;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public abstract class AbstractStatusBarSourceGenerator
    extends AbstractSourceGenerator {

  protected String controllerPackage;

  public void generate()
      throws GeneratorException {
    this.controllerPackage = this.clientPackageJavaConform + ".ui.statusbar";

    this.generateIComponentClass();
    this.generateComponentClass();
    this.generateControllerClass();
  }

  private void generateIComponentClass()
      throws GeneratorException {
    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder("IStatusbarComponent")
                                        .addJavadoc(CodeBlock.builder()
                                                             .add(GeneratorConstants.COPYRIGHT_JAVA)
                                                             .build())
                                        .addModifiers(Modifier.PUBLIC)
                                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(IsComponent.class),
                                                                                     ClassName.get(this.controllerPackage,
                                                                                                   "IStatusbarComponent.Controller"),
                                                                                     super.getClassNameWidget()))
                                        .addMethod(MethodSpec.methodBuilder("edit")
                                                             .addModifiers(Modifier.PUBLIC,
                                                                           Modifier.ABSTRACT)
                                                             .addParameter(ParameterSpec.builder(String.class,
                                                                                                 "message")
                                                                                        .build())
                                                             .build());

    typeSpec.addType(TypeSpec.interfaceBuilder("Controller")
                             .addSuperinterface(ClassName.get(IsComponent.Controller.class))
                             .addModifiers(Modifier.PUBLIC,
                                           Modifier.STATIC)
                             .build());

    JavaFile javaFile = JavaFile.builder(this.controllerPackage,
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(new File(directoryJava,
                                ""));
    } catch (IOException e) {
      throw new GeneratorException("Unable to write generated file: >>IStatusbarComponent" + "<< -> " + "exception: " + e.getMessage());
    }
  }

  private void generateComponentClass()
      throws GeneratorException {
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("StatusbarComponent")
                                        .addJavadoc(CodeBlock.builder()
                                                             .add(GeneratorConstants.COPYRIGHT_JAVA)
                                                             .build())
                                        .addModifiers(Modifier.PUBLIC)
                                        .superclass(ParameterizedTypeName.get(ClassName.get(AbstractComponent.class),
                                                                              ClassName.get(this.controllerPackage,
                                                                                            "IStatusbarComponent.Controller"),
                                                                              super.getClassNameWidget()))
                                        .addSuperinterface(ClassName.get(this.controllerPackage,
                                                                         "IStatusbarComponent"));
    typeSpec.addField(getLabelFieldSpec());
    // constrcutor
    typeSpec.addMethod(MethodSpec.constructorBuilder()
                                 .addStatement("super()")
                                 .addModifiers(Modifier.PUBLIC)
                                 .build());
    // edit(String) method
    typeSpec.addMethod(MethodSpec.methodBuilder("edit")
                                 .addAnnotation(Override.class)
                                 .addModifiers(Modifier.PUBLIC)
                                 .addParameter(ParameterSpec.builder(String.class,
                                                                     "message")
                                                            .build())
                                 .addStatement(getSetLabelValueStatement())
                                 .build());
    // createComponent method
    createRenderMethod(typeSpec);

    JavaFile javaFile = JavaFile.builder(this.controllerPackage,
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(new File(directoryJava,
                                ""));
    } catch (IOException e) {
      throw new GeneratorException("Unable to write generated file: >>StatusbarComponent" + "<< -> " + "exception: " + e.getMessage());
    }
  }

  private void generateControllerClass()
      throws GeneratorException {
    TypeSpec.Builder typeSpec = TypeSpec.classBuilder("StatusbarController")
                                        .addJavadoc(CodeBlock.builder()
                                                             .add(GeneratorConstants.COPYRIGHT_JAVA)
                                                             .build())
                                        .addModifiers(Modifier.PUBLIC)
                                        .addAnnotation(AnnotationSpec.builder(Controller.class)
                                                                     .addMember("route",
                                                                                "$S",
                                                                                "/application/")
                                                                     .addMember("selector",
                                                                                "$S",
                                                                                "footer")
                                                                     .addMember("componentInterface",
                                                                                "$T.class",
                                                                                ClassName.get(this.controllerPackage,
                                                                                              "IStatusbarComponent"))
                                                                     .addMember("component",
                                                                                "$T.class",
                                                                                ClassName.get(this.controllerPackage,
                                                                                              "StatusbarComponent"))
                                                                     .build())
                                        .superclass(ParameterizedTypeName.get(ClassName.get(AbstractComponentController.class),
                                                                              ClassName.get(this.clientPackageJavaConform,
                                                                                            GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()) +
                                                                                            GeneratorConstants.CONTEXT),
                                                                              ClassName.get(this.controllerPackage,
                                                                                            "IStatusbarComponent"),
                                                                              super.getClassNameWidget()))
                                        .addSuperinterface(ClassName.get(this.controllerPackage,
                                                                         "IStatusbarComponent.Controller"))
                                        .addField(FieldSpec.builder(ClassName.get(HandlerRegistration.class),
                                                                    "registration",
                                                                    Modifier.PRIVATE)
                                                           .build())
                                        .addMethod(MethodSpec.constructorBuilder()
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .build())
                                        .addMethod(MethodSpec.methodBuilder("start")
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .addAnnotation(ClassName.get(Override.class))
                                                             .addStatement("this.registration = this.eventBus.addHandler($T.TYPE, e -> component.edit(e.getStatus()))",
                                                                           ClassName.get(this.clientPackageJavaConform + ".event",
                                                                                         "StatusChangeEvent"))
                                                             .build())
                                        .addMethod(MethodSpec.methodBuilder("stop")
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .addAnnotation(ClassName.get(Override.class))
                                                             .addStatement("this.registration.removeHandler()")
                                                             .build());

    JavaFile javaFile = JavaFile.builder(this.controllerPackage,
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(new File(directoryJava,
                                ""));
    } catch (IOException e) {
      throw new GeneratorException("Unable to write generated file: >>StatusbarController" + "<< -> " + "exception: " + e.getMessage());
    }
  }

  protected abstract FieldSpec getLabelFieldSpec();

  protected abstract String getSetLabelValueStatement();

  protected abstract void createRenderMethod(TypeSpec.Builder typeSpec);

}
