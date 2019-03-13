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

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.List;

public class PubSubProducer {
  String topic = null;
  String projectId = null;
  ProjectTopicName topicName = null;
  Publisher publisher = null;

  public PubSubProducer(String topic, String projectId){
    this.topic = topic;
    this.projectId = projectId;
    this.topicName = ProjectTopicName.of(this.projectId, this.topic);
    // Create a publisher instance with default settings bound to the topic
    try {
      this.publisher = Publisher.newBuilder(topicName).build();
    } catch(Exception iException){
      System.out.println("Could not initialize PubSub publisher:" + iException.getMessage());
      iException.printStackTrace();
    }
  }

  public void publish(List<String> messages) throws Exception {

    try {
      

      for (final String message : messages) {
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        
        // Once published, returns a server-assigned message id (unique within the topic)
        ApiFuture<String> future = publisher.publish(pubsubMessage);

        // Add an asynchronous callback to handle success / failure
        ApiFutures.addCallback(
            future,
            new ApiFutureCallback<String>() {

              @Override
              public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {
                  ApiException apiException = ((ApiException) throwable);
                  // details on the API exception
                  System.out.println(apiException.getStatusCode().getCode());
                  System.out.println(apiException.isRetryable());
                }
                System.out.println("Error publishing message : " + message);
              }

              @Override
              public void onSuccess(String messageId) {
                // Once published, returns server-assigned message ids (unique within the topic)
                System.out.println(messageId);
              }
            },
            MoreExecutors.directExecutor());
      }
    } finally {
      if (publisher != null) {
        // When finished with the publisher, shutdown to free up resources.
        //If we close it here it will have a hard time bringing it back up.
        //publisher.shutdown();
        //publisher.awaitTermination(30, TimeUnit.MINUTES);
      }
    }
  }
}