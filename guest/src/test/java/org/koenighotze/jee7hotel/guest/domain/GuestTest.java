package org.koenighotze.jee7hotel.guest.domain;

import org.junit.Test;
import org.koenighotze.jee7hotel.framework.persistence.audit.Audit;
import org.koenighotze.jee7hotel.framework.test.AbstractBasePersistenceTest;

import java.io.StringWriter;

import static java.time.LocalDateTime.now;
import static javax.xml.bind.JAXB.marshal;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class GuestTest extends AbstractBasePersistenceTest {

    @Test
    public void persisting_a_guest_sets_the_last_update_field() {
        Guest guest = new Guest("", "", "");

        new Audit().setAuditFields(guest);

        assertThat(guest.getLastUpdate()).isNotNull();
    }

    @Test
    public void a_guest_can_be_persisted() {
        Guest guest = new Guest("foo", "bar", "qux");

        new Audit().setAuditFields(guest);

        getEntityManager().persist(guest);

        getEntityManager().flush();
    }

    @Test
    public void a_guest_can_be_transformed_to_xml() {
        StringWriter w = new StringWriter();
        Guest guest = new Guest("foo", "bar", "qux");
        guest.setLastUpdate(now());
        marshal(guest, w);
        assertThat(w.toString()).startsWith("<?xml");
    }

}