package org.apache.camel.component.rabbitmq.reply;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQEndpoint;


/**
 * The {@link ReplyManager} is responsible for handling <a href="http://camel.apache.org/request-reply.html">request-reply</a>
 * over RabbitMQ.
 *
 * @version 
 */
public interface ReplyManager {

	/**
     * Sets the belonging {@link org.apache.camel.component.jms.JmsEndpoint}.
     */
    void setEndpoint(RabbitMQEndpoint endpoint);
    
    /**
     * Sets the reply to queue the manager should listen for replies.
     * <p/>
     * The queue is either a temporary or a persistent queue.
     */
    void setReplyTo(String replyTo);
    
    /**
     * Gets the reply to queue being used
     */
    String getReplyTo();
    
	/**
     * Register a reply
     *
     * @param replyManager    the reply manager being used
     * @param exchange        the exchange
     * @param callback        the callback
     * @param originalCorrelationId  an optional original correlation id
     * @param correlationId   the correlation id to expect being used
     * @param requestTimeout  the timeout
     * @return the correlation id used
     */
    String registerReply(ReplyManager replyManager, Exchange exchange, AsyncCallback callback,
                         String originalCorrelationId, String correlationId, long requestTimeout);
    
    /**
     * Sets the scheduled to use when checking for timeouts (no reply received within a given time period)
     */
    void setScheduledExecutorService(ScheduledExecutorService executorService);


    /**
     * Updates the correlation id to the new correlation id.
     * <p/>
     * This is only used when <tt>useMessageIDasCorrelationID</tt> option is used, which means a
     * provisional correlation id is first used, then after the message has been sent, the real
     * correlation id is known. This allows us then to update the internal mapping to expect the
     * real correlation id.
     *
     * @param correlationId     the provisional correlation id
     * @param newCorrelationId  the real correlation id
     * @param requestTimeout    the timeout
     */
    void updateCorrelationId(String correlationId, String newCorrelationId, long requestTimeout);
    

    /**
     * Process the reply
     *
     * @param holder  containing needed data to process the reply and continue routing
     */
    void processReply(ReplyHolder holder);
}