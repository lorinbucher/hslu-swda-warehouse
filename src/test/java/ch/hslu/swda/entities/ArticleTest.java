package ch.hslu.swda.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test cases for the article entity.
 */
public class ArticleTest {

    @Test
    void testArticleIdInvalid() {
        assertThatThrownBy(() -> new Article(0L, "Test", new BigDecimal("1.00"), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("articleId should not be lower than 1");
    }

    @Test
    void testArticleIdValid() {
        final Article article = new Article(1L, "Test", new BigDecimal("1.00"), 1);
        assertThat(article.articleId()).isEqualTo(1L);
    }

    @Test
    void testNameNull() {
        assertThatThrownBy(() -> new Article(1L, null, new BigDecimal("1.00"), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name should not be blank");
    }

    @Test
    void testNameInvalid() {
        assertThatThrownBy(() -> new Article(1L, "", new BigDecimal("1.00"), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name should not be blank");
    }

    @Test
    void testNameValid() {
        final Article article = new Article(1L, "Test", new BigDecimal("1.00"), 1);
        assertThat(article.name()).isEqualTo("Test");
    }

    @Test
    void testPriceNull() {
        assertThatThrownBy(() -> new Article(1L, "Test", null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("price should be 0.05 or higher");
    }

    @Test
    void testPriceNegative() {
        assertThatThrownBy(() -> new Article(1L, "Test", new BigDecimal("-1.00"), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("price should be 0.05 or higher");
    }

    @Test
    void testPriceZero() {
        assertThatThrownBy(() -> new Article(1L, "Test", new BigDecimal("0.00"), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("price should be 0.05 or higher");
    }

    @Test
    void testPriceValid() {
        final Article article = new Article(1L, "Test", new BigDecimal("0.05"), 1);
        assertThat(article.price().compareTo(new BigDecimal("0.05"))).isEqualTo(0);
    }

    @Test
    void testPriceRounded() {
        final Article article = new Article(1L, "Test", new BigDecimal("15.2649895"), 1);
        assertThat(article.price().compareTo(new BigDecimal("15.26"))).isEqualTo(0);
    }

    @Test
    void testStockInvalid() {
        assertThatThrownBy(() -> new Article(1L, "Test", new BigDecimal("1.00"), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stock should not be lower than 0");
    }

    @Test
    void testStockValid() {
        final Article article = new Article(1L, "Test", new BigDecimal("1.00"), 0);
        assertThat(article.stock()).isEqualTo(0);
    }

    @Test
    void testArticleNotEqual() {
        final Article article1 = new Article(1L, "Test1", new BigDecimal("1.00"), 0);
        final Article article2 = new Article(2L, "Test2", new BigDecimal("2.00"), 1);
        assertThat(article1).isNotEqualTo(article2);
    }

    @Test
    void testArticleEqual() {
        final Article article1 = new Article(1L, "Test1", new BigDecimal("1.00"), 0);
        final Article article2 = new Article(1L, "Test2", new BigDecimal("2.00"), 1);
        assertThat(article1).isEqualTo(article2);
    }

    @Test
    void testArticleHashCodeDiffers() {
        final Article article1 = new Article(1L, "Test1", new BigDecimal("1.00"), 0);
        final Article article2 = new Article(2L, "Test2", new BigDecimal("2.00"), 1);
        assertThat(article1).doesNotHaveSameHashCodeAs(article2);
    }

    @Test
    void testArticleHashCode() {
        final Article article1 = new Article(1L, "Test1", new BigDecimal("1.00"), 0);
        final Article article2 = new Article(1L, "Test2", new BigDecimal("2.00"), 1);
        assertThat(article1).hasSameHashCodeAs(article2);
    }

    @Test
    void testJsonObject() {
        final Article article = new Article(1L, "Test", new BigDecimal("50.25"), 5);
        String articleJson = "{\"articleId\":1,\"name\":\"Test\",\"price\":50.25,\"stock\":5}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            assertThat(mapper.writeValueAsString(article)).isEqualTo(articleJson);
        } catch (JsonProcessingException e) {
            assertThat(e).isNull();
        }
    }
}
