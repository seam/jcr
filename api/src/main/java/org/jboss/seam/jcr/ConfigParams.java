/*
  JBoss, Home of Professional Open Source
  Copyright [2010], Red Hat, Inc., and individual contributors
  by the @authors tag. See the copyright.txt in the distribution for a
  full listing of individual contributors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.jboss.seam.jcr;

/**
 * A set of ConfigParams for both Jackrabbit and ModeShape to avoid duplicating the setting value.
 * 
 * @author johnament
 */
public abstract class ConfigParams {
    private ConfigParams() {
    }

    /**
     * Represents the Jackrabbit Repository Conf parameter in the settings.
     */
    public static final String JACKRABBIT_REPOSITORY_CONF = "org.apache.jackrabbit.repository.conf";
    /**
     * Represents the Jackrabbit Repository Home parameter.
     */
    public static final String JACKRABBIT_REPOSITORY_HOME = "org.apache.jackrabbit.repository.home";
    /**
     * Represents the ModeShape URL Parameter.
     */
    public static final String MODESHAPE_URL = "org.modeshape.jcr.URL";

    /**
     * Represents the current workspace name
     */
    public static final String WORKSPACE_NAME = "javax.jcr.Workspace";
}