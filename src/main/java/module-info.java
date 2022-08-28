module com.nuah {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.jgit;
    requires org.apache.commons.io;

    opens com.nuah to javafx.fxml;
    exports com.nuah;
}
