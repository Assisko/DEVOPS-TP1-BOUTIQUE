
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ArticleTest.class,
        PanierTest.class,
        PanierReductionTest.class,
        ServiceCommandeTest.class
})
class SuiteBoutique {
}