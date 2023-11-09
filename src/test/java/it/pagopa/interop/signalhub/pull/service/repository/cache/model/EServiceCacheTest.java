package it.pagopa.interop.signalhub.pull.service.repository.cache.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EServiceCacheTest {

    @Test
    void testNotEquals() {
        EServiceCache x = new EServiceCache();
        x.setProducerId("123");
        x.setEserviceId("123");
        x.setDescriptorId("123");
        EServiceCache y = new EServiceCache();
        y.setProducerId("123");
        y.setEserviceId("123");
        y.setDescriptorId("124");
        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertFalse(x.equals(null) && y.equals(null));

    }

    @Test
    public void testEqualsAndHashCode() {
        EServiceCache x = new EServiceCache();
        x.setProducerId("123");
        x.setEserviceId("123");
        x.setDescriptorId("123");
        EServiceCache y = new EServiceCache();
        y.setProducerId("123");
        y.setEserviceId("123");
        y.setDescriptorId("123");
        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertTrue(x.hashCode() == y.hashCode());
    }
}