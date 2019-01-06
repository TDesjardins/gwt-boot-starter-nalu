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

package com.github.nalukit.gwtbootstarternalu.client.ui.content;

import com.github.nalukit.gwtbootstarternalu.client.ApplicationContext;
import com.github.nalukit.gwtbootstarternalu.client.event.FlushProjectEvent;
import com.github.nalukit.gwtbootstarternalu.client.event.GenerateProjectEvent;
import com.github.nalukit.gwtbootstarternalu.client.ui.content.composite.application.ApplicationComposite;
import com.github.nalukit.gwtbootstarternalu.client.ui.content.composite.project.ProjectComposite;
import com.github.nalukit.gwtbootstarternalu.client.ui.content.composite.screen.ScreenComposite;
import com.github.nalukit.gwtbootstarternalu.shared.model.ControllerData;
import com.github.nalukit.nalu.client.component.AbstractComponentController;
import com.github.nalukit.nalu.client.component.annotation.Composite;
import com.github.nalukit.nalu.client.component.annotation.Composites;
import com.github.nalukit.nalu.client.component.annotation.Controller;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.notifications.Notification;

import java.util.Optional;

@Controller(route = "/application/setUp",
            selector = "content",
            componentInterface = IContentComponent.class,
            component = ContentComponent.class)
@Composites({ @Composite(name = "ApplicationMetaData",
                         compositeController = ApplicationComposite.class,
                         selector = "applicationSettings"),
              @Composite(name = "ProjectMetaData",
                         compositeController = ProjectComposite.class,
                         selector = "projectSettings"),
              @Composite(name = "ScreenMetaData",
                         compositeController = ScreenComposite.class,
                         selector = "screenSettings") })
public class ContentController
    extends AbstractComponentController<ApplicationContext, IContentComponent, HTMLElement>
    implements IContentComponent.Controller {

  public ContentController() {
  }

  @Override
  public void start() {
    this.eventBus.addHandler(FlushProjectEvent.TYPE,
                             e -> {
                               // flush data
                               this.<ApplicationComposite>getComposite("ApplicationMetaData").flush();
                               this.<ProjectComposite>getComposite("ProjectMetaData").flush();
                               this.<ScreenComposite>getComposite("ScreenMetaData").flush();
                               // check is >>login<< is used as route
                               Optional<String> optionalRouteLogin = this.context.getNaluGeneraterParms()
                                                                                 .getControllers()
                                                                                 .stream()
                                                                                 .map(ControllerData::getRoute)
                                                                                 .filter(s -> "login".equals(s.toLowerCase()))
                                                                                 .findAny();
                               if (optionalRouteLogin.isPresent()) {
                                 Notification.createDanger("route value >>login<< is a reserved word")
                                             .setPosition(Notification.TOP_LEFT)
                                             .show();
                                 return;
                               }
                               // check is >>error<< is used as route
                               Optional<String> optionalRouteError = this.context.getNaluGeneraterParms()
                                                                                 .getControllers()
                                                                                 .stream()
                                                                                 .map(ControllerData::getRoute)
                                                                                 .filter(s -> "error".equals(s.toLowerCase()))
                                                                                 .findAny();
                               if (optionalRouteError.isPresent()) {
                                 Notification.createDanger("route value >>error<< is a reserved word")
                                             .setPosition(Notification.TOP_LEFT)
                                             .show();
                                 return;
                               }

                               eventBus.fireEvent(new GenerateProjectEvent());
                             });
    this.<ApplicationComposite>getComposite("ApplicationMetaData").edit();
    this.<ProjectComposite>getComposite("ProjectMetaData").edit();
    this.<ScreenComposite>getComposite("ScreenMetaData").edit();
  }
}
