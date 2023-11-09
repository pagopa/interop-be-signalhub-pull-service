package it.pagopa.interop.signalhub.pull.service.repository.cache.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerEServiceCacheTest {

    @Test
    void testNotEquals() {
        ConsumerEServiceCache x = new ConsumerEServiceCache();
        x.setConsumerId("123");
        x.setEserviceId("123");
        x.setDescriptorId("123");
        ConsumerEServiceCache y = new ConsumerEServiceCache();
        y.setConsumerId("123");
        y.setEserviceId("123");
        y.setDescriptorId("124");
        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertFalse(x.equals(null) && y.equals(null));

    }

    @Test
    public void testEqualsAndHashCode() {
        ConsumerEServiceCache x = new ConsumerEServiceCache();
        x.setConsumerId("123");
        x.setEserviceId("123");
        x.setDescriptorId("123");
        ConsumerEServiceCache y = new ConsumerEServiceCache();
        y.setConsumerId("123");
        y.setEserviceId("123");
        y.setDescriptorId("123");
        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }
}