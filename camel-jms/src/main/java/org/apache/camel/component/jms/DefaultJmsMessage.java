/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jms;

import org.apache.camel.InvalidHeaderTypeException;
import org.apache.camel.impl.MessageSupport;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision:520964 $
 */
public class DefaultJmsMessage extends MessageSupport implements JmsMessage {
    private Message jmsMessage;
    private Map<String, Object> lazyHeaders;

    public DefaultJmsMessage() {
    }

    public DefaultJmsMessage(Message jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

    public Message getJmsMessage() {
        return jmsMessage;
    }

    public void setJmsMessage(Message jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

    public Object getHeader(String name) {
        if (jmsMessage != null) {
            try {
                Object value = jmsMessage.getObjectProperty(name);
                try {
                    return value;
                }
                catch (ClassCastException e) {
                    throw new InvalidHeaderTypeException(e.getMessage(), value);
                }
            }
            catch (JMSException e) {
                throw new MessagePropertyAcessException(name, e);
            }
        }
        return null;
    }

    public void setHeader(String name, Object value) {
        if (jmsMessage != null) {
            try {
                jmsMessage.setObjectProperty(name, value);
            }
            catch (JMSException e) {
                throw new MessagePropertyAcessException(name, e);
            }
        }
        else {
            if (lazyHeaders == null) {
                lazyHeaders = new HashMap<String, Object>();
            }
            lazyHeaders.put(name, value);
        }
    }

    public Map<String, Object> getHeaders() {
        if (jmsMessage != null) {
            Map<String, Object> answer = new HashMap<String, Object>();
            Enumeration names;
            try {
                names = jmsMessage.getPropertyNames();
            }
            catch (JMSException e) {
                throw new MessagePropertyNamesAcessException(e);
            }
            while (names.hasMoreElements()) {
                String name = names.nextElement().toString();
                try {
                    Object value = jmsMessage.getObjectProperty(name);
                    answer.put(name, value);
                }
                catch (JMSException e) {
                    throw new MessagePropertyAcessException(name, e);
                }
            }
            return answer;
        }
        else {
            return lazyHeaders;
        }
    }

    @Override
    public DefaultJmsMessage newInstance() {
        return new DefaultJmsMessage();
    }
}
