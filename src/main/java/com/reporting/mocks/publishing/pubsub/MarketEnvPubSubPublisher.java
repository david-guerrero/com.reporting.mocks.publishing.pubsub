/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.reporting.mocks.publishing.pubsub;

import com.google.gson.Gson;
import com.reporting.mocks.interfaces.publishing.IResultPublisherConfiguration;
import com.reporting.mocks.model.MarketEnv;

import java.util.ArrayList;
import java.util.List;

public class MarketEnvPubSubPublisher {
    private PubSubProducer producer;

    public MarketEnvPubSubPublisher(IResultPublisherConfiguration resultPublisherConfiguration, PubsubConfig appConfig) {
        producer = new PubSubProducer(resultPublisherConfiguration.getMarketEnvTopic(), appConfig.getProjectId());
    }

    public void send(MarketEnv marketEnv) {
        if (this.producer != null) {
            Gson gson = new Gson();
            List<String> records = new ArrayList<String>();
            records.add(gson.toJson(marketEnv));
            try {
                this.producer.publish(records);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean close(){
        try {
            this.producer.publisher.shutdown();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
