package entities;

public class FooterEntity extends BaseEntity {
    public String linkOfElement;
    public String textOfXpath;

    @Override
    public String toString() {
        return " FooterEntity{" +
                "linkOfElement='" + linkOfElement + '\'' +
                ", textOfXpath='" + textOfXpath + '\'' +
                ", openInNewTab=" + openInNewTab +
                '}';
    }

    public boolean openInNewTab;
}
