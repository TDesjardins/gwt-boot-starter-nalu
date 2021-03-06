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

package com.github.nalukit.bootstarternalu.server.resource.generator.impl.gxt;

import com.github.nalukit.bootstarternalu.server.resource.generator.impl.AbstractStatusBarSourceGenerator;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;

public class StatusBarGxtSourceGenerator
    extends AbstractStatusBarSourceGenerator {

  private StatusBarGxtSourceGenerator(Builder builder) {
    super();

    this.naluGeneraterParms = builder.naluGeneraterParms;
    this.directoryJava = builder.directoryJava;
    this.clientPackageJavaConform = builder.clientPackageJavaConform;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  protected FieldSpec getLabelFieldSpec() {
    return FieldSpec.builder(Label.class,
                             "label",
                             Modifier.PRIVATE)
                    .build();
  }

  @Override
  protected String getSetLabelValueStatement() {
    return "label.setText(message)";
  }

  @Override
  protected void createRenderMethod(TypeSpec.Builder typeSpec) {
    MethodSpec.Builder method = MethodSpec.methodBuilder("render")
                                          .addAnnotation(Override.class)
                                          .addModifiers(Modifier.PUBLIC)
                                          .addStatement("$T container = new $T()",
                                                        ClassName.get(SimpleContainer.class),
                                                        ClassName.get(SimpleContainer.class))
                                          .addStatement("label = new $T()",
                                                        Label.class)
                                          .addStatement("label.getElement().getStyle().setMargin(12, $T.PX)",
                                                        Style.Unit.class)
                                          .addStatement("container.setWidget(label)")
                                          .addStatement("initElement(container)");

    typeSpec.addMethod(method.build());
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

    public StatusBarGxtSourceGenerator build() {
      return new StatusBarGxtSourceGenerator(this);
    }

  }

}
