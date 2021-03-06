/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.servicecomb.serviceregistry.task;

import com.google.common.eventbus.EventBus;

import io.servicecomb.serviceregistry.api.registry.Microservice;
import io.servicecomb.serviceregistry.client.ServiceRegistryClient;

public abstract class AbstractTask implements Runnable {
    protected TaskStatus taskStatus = TaskStatus.INIT;

    protected EventBus eventBus;

    protected ServiceRegistryClient srClient;

    protected Microservice microservice;

    public AbstractTask(EventBus eventBus, ServiceRegistryClient srClient, Microservice microservice) {
        this.eventBus = eventBus;
        this.srClient = srClient;
        this.microservice = microservice;

        this.eventBus.register(this);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Microservice getMicroservice() {
        return microservice;
    }

    @Override
    public void run() {
        if(taskStatus == TaskStatus.READY) {
            // if this task is actually run, we send a notification
            doRun();
            eventBus.post(this);
        }
    }

    abstract protected void doRun();

    protected boolean isSameMicroservice(Microservice otherMicroservice) {
        return microservice.getServiceName().equals(otherMicroservice.getServiceName());
    }
}
