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

package com.github.nalukit.bootstarternalu.server.resource.generator.impl.domino;

import com.github.nalukit.bootstarternalu.server.resource.generator.impl.AbstractShellApplicationSourceGenerator;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import com.github.nalukit.nalu.client.component.annotation.Shell;
import com.squareup.javapoet.*;
import elemental2.dom.CSSProperties;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.style.ColorScheme;

import javax.lang.model.element.Modifier;
import java.io.File;

public class ShellErrorDominoSourceGenerator
    extends AbstractShellApplicationSourceGenerator {

  private ShellErrorDominoSourceGenerator(Builder builder) {
    super();

    this.naluGeneraterParms = builder.naluGeneraterParms;
    this.directoryJava = builder.directoryJava;
    this.clientPackageJavaConform = builder.clientPackageJavaConform;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  protected void createFieldSpecs(TypeSpec.Builder typeSpec) {
    typeSpec.addField(FieldSpec.builder(ClassName.get(Layout.class),
                                        "layout")
                               .addModifiers(Modifier.PRIVATE)
                               .build());
  }

  @Override
  public MethodSpec createAttachShellMethod() {
    return MethodSpec.methodBuilder("attachShell")
                     .addAnnotation(ClassName.get(Override.class))
                     .addModifiers(Modifier.PUBLIC)
                     .addStatement("layout = $T.create(\"Nalu - Simple Application using Domino-UI - Error Message\")\n" + "                          .show($T.INDIGO)",
                                   ClassName.get(Layout.class),
                                   ClassName.get(ColorScheme.class))
                     .addCode("")
                     .addStatement("layout.showFooter()\n" +
                                   "          .fixFooter()\n" +
                                   "          .getFooter()\n" +
                                   "          .asElement().style.minHeight = $T.MinHeightUnionType.of(\"42px\")",
                                   ClassName.get(CSSProperties.class))
                     .addStatement("layout.getFooter().setId(\"footer\")")
                     .addStatement("layout.disableLeftPanel()")
                     .addStatement("layout.getContentPanel().setId(\"content\")")
                     .build();
  }

  @Override
  protected MethodSpec createDetachMethod() {
    return MethodSpec.methodBuilder("detachShell")
                     .addAnnotation(ClassName.get(Override.class))
                     .addModifiers(Modifier.PUBLIC)
                     .addStatement("this.layout.remove()")
                     .build();
  }

  @Override
  protected String getShellName() {
    return super.shellPackage + getShellSimpleName();
  }

  @Override
  protected void generateShellAnnotation(TypeSpec.Builder typeSpec) {
    typeSpec.addAnnotation(AnnotationSpec.builder(Shell.class)
                                         .addMember("value",
                                                    "$S",
                                                    "error")
                                         .build());
  }

  @Override
  protected String getShellSimpleName() {
    return "ErrorShell";
  }

  @Override
  protected void setUpShellPackage() {
    super.shellPackage = this.clientPackageJavaConform + ".ui.shell.error";

  }

  @Override
  public MethodSpec createRenderMethod() {
    return null;
  }

  @Override
  public MethodSpec createForceLayoutMethod() {
    return null;
  }

  @Override
  protected MethodSpec createBindMethod() {
    return null;
  }

  @Override
  protected void createAddMethods(TypeSpec.Builder typeSpec) {
  }

  public static class Builder {

    NaluGeneraterParms naluGeneraterParms;

    File directoryJava;

    String clientPackageJavaConform;

    public Builder naluGeneraterParms(NaluGeneraterParms naluGeneraterParms) {
      this.naluGeneraterParms = naluGeneraterParms;
      return this;
    }

    public Builder directoryJava(File directoryJava) {
      this.directoryJava = directoryJava;
      return this;
    }

    public Builder clientPackageJavaConform(String clientPackageJavaConform) {
      this.clientPackageJavaConform = clientPackageJavaConform;
      return this;
    }

    public ShellErrorDominoSourceGenerator build() {
      return new ShellErrorDominoSourceGenerator(this);
    }

  }

}
