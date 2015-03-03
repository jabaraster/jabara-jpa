/**
 * 
 */
package jabara.jpa.entity;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.core.Is.is;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "static-method", "boxing" })
public class IdTest {

    /**
     * -
     */
    @Test
    public void _compareTo_レシーバーが小さい() {
        assertThat(new Id<TestEntity>(-1).compareTo(new Id<TestEntity>(0)) < 0, is(true));
    }

    /**
     * -
     */
    @Test
    public void _compareTo_レシーバーが大きい() {
        assertThat(new Id<TestEntity>(10).compareTo(new Id<TestEntity>(0)) > 0, is(true));
    }

    /**
     * Test method for {@link jabara.jpa.entity.Id#compareTo(jabara.jpa.entity.Id)}.
     */
    @Test
    public void _compareTo_同じ大きさ() {
        assertThat(new Id<TestEntity>(0).compareTo(new Id<TestEntity>(0)), is(0));
    }

    private static class TestEntity extends EntityBase<TestEntity> {
        private static final long serialVersionUID = 6397507609600244747L;

    }
}
