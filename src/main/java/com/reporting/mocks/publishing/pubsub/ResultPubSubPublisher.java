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

import com.reporting.mocks.interfaces.publishing.IResultPublisher;
import com.reporting.mocks.interfaces.publishing.IResultPublisherConfiguration;
import com.reporting.mocks.model.*;
import org.springframework.stereotype.Component;

@Component
public class ResultPubSubPublisher implements IResultPublisher {
    protected RiskResultSetPubSubProducer riskResultSetProducer;
    protected RiskResultPubSubProducer riskResultProducer;
    protected CalculationContextPubSubProducer calculationContextProducer;
    protected TradePubSubPublisher tradePubSubPublisher;
    protected MarketEnvPubSubPublisher marketEnvPubSubPublisher;
    protected IResultPublisherConfiguration resultPublisherConfiguration;

    public ResultPubSubPublisher(IResultPublisherConfiguration resultPublisherConfiguration, PubsubConfig appConfig) {
//        PubsubConfig appConfig = new PubsubConfig();

        System.out.println(appConfig.getProjectId());
        this.resultPublisherConfiguration = resultPublisherConfiguration;
        this.riskResultSetProducer = new RiskResultSetPubSubProducer(resultPublisherConfiguration, appConfig);
        this.riskResultProducer = new RiskResultPubSubProducer(resultPublisherConfiguration, appConfig);
        this.calculationContextProducer = new CalculationContextPubSubProducer(resultPublisherConfiguration, appConfig);
        this.tradePubSubPublisher = new TradePubSubPublisher(resultPublisherConfiguration, appConfig);
        this.marketEnvPubSubPublisher = new MarketEnvPubSubPublisher(resultPublisherConfiguration, appConfig);
    }

    public boolean close(){
        return 
            this.riskResultProducer.close() &&
            this.calculationContextProducer.close() &&
            this.marketEnvPubSubPublisher.close() &&
            this.riskResultSetProducer.close() &&
            this.tradePubSubPublisher.close();
    }

    @Override
    public void publish(CalculationContext calculationContext) {
        this.calculationContextProducer.send(calculationContext);
    }

    @Override
    public void publish(MarketEnv marketEnv) {
        this.marketEnvPubSubPublisher.send(marketEnv);
    }

    @Override
    public void publishIntradayRiskResultSet(RiskResultSet riskResultSet) {
        this.riskResultSetProducer.send(riskResultSet);
    }


    @Override
    public void publishIntradayRiskResult(RiskResult riskResult) { 
        this.riskResultProducer.send(riskResult); 
    }

    @Override
    public void publishIntradayTrade(TradeLifecycle tradeLifecycle) {
        this.tradePubSubPublisher.send(tradeLifecycle);
    }

    @Override
    public void publishEndofDayRiskRun(RiskResultSet riskResultSet) {

    }
}
