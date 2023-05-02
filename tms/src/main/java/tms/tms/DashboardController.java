package tms.tms;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileReader;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.reactfx.Subscription;
import org.reactfx.collection.ListModification;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;

import javafx.util.Duration;

import org.controlsfx.control.textfield.CustomTextField;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.text.DecimalFormat;

import java.sql.SQLException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javafx.scene.layout.HBox.setMargin;

public class DashboardController implements Cloneable {
    private static final Duration BUTTONS_TRANS_DURATION = Duration.seconds(0.2);
    private static final Duration VBOX_TRANS_DURATION = Duration.seconds(0.3);
    private static final String STYLE_BUTTON_OK = "-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px;";
    private static final String STYLE_BUTTON_CANCEL = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px;";
    private static final String STYLE_DIALOG_LAYOUT = "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-padding: 20px;";
    private static final String TEAM_ICON_BASE_PATH = "file:/C:/Users/chedl/Downloads/tms/target/classes/tms/tms/images/";
    private static final String TEAM_ICON_DEFAULT_IMAGE = "T_old.png";
    private static final String CSS_WHITE_BACKGROUND = "-fx-background-color: white;";
    private static final String CSS_HAND_CURSOR = "-fx-cursor: hand;";
    private static final String CSS_FONT_SIZE_14 = "-fx-font-size: 14px;";
    private static final String CSS_BORDER_LIGHTGRAY = "-fx-border-width: 2 2 2 2; -fx-border-color: lightgray;";
    private static final String CSS_TAB_MIN_WIDTH_0 = "-fx-tab-min-width: 0;";
    private static final String CSS_TAB_PANE = CSS_WHITE_BACKGROUND + CSS_BORDER_LIGHTGRAY + CSS_FONT_SIZE_14 + CSS_TAB_MIN_WIDTH_0;
    private static final String CSS_SCROLL_PANE = "fx-background-color: #eeeeee; -fx-background-insets: 0; -fx-box-shadow: transparent 0px 0px 0px inset; -fx-transition: box-shadow 300ms ease 0s; -fx-z-index: 1; -fx-overflow: hidden; -fx-margin-right: 0px; -fx-margin-bottom: 0px;";
    private final int MAX_TEAM_DESCRIPTION_LENGTH = 200;
    @FXML private Label leaveTeamText;
    @FXML private Label leaveTeamTextDescription;
    @FXML private Label archiveTeamText;
    @FXML private Label archiveTeamTextDescription;
    @FXML private JFXButton leaveTeamButton;
    @FXML private JFXButton archiveTeamButton;
    @FXML private TextField teamSettingsDescription;
    @FXML private Tab iconSettingsTab;
    @FXML private Tab emojiSettingsTab;
    @FXML private Tab customSettingsTab;
    @FXML private ScrollPane iconSettingsScroll;
    @FXML private TilePane iconSettingsTile;
    @FXML private TilePane emojiSettingsTile;
    @FXML private ScrollPane emojiSettingsScroll;
    @FXML private JFXButton submitSettingsLinkButton;
    @FXML private JFXButton uploadSettingsIconButton;
    @FXML private CustomTextField iconSettingsLink;
    @FXML private JFXButton teamSettingsIconButton;
    @FXML private TabPane teamSettingsLogoPane;
    @FXML private JFXButton teamSettingsUpdateButton;
    @FXML private TextField teamSettingsTeamNameField;
    @FXML private ImageView teamSettingsTeamIcon;
    @FXML private Label teamSettingsTeamName;
    @FXML private Pane teamHiderPane;
    @FXML private Pane teamSettingsPane;
    @FXML private VBox teamSettingsVBox;
    @FXML private VBox BrowseTeamSpaceVBox;
    @FXML private Button teamNameButton;
    @FXML private StackPane teamNamesStackPane;
    @FXML private VBox browseTeamsListVBox;
    @FXML private Button closeTeamsButton;
    @FXML private Pane browseTeamsPane;
    @FXML private VBox browseTeamsVBox;
    @FXML private Button browseTeamsButton;
    @FXML private JFXButton browseTeamsEllipsis;
    @FXML private Pane dateFilterOptionsPane;
    @FXML private Pane dateFilterMenuPane;
    @FXML private Button dateOptionsButton;
    @FXML private Button dateFilterButton;
    @FXML private Pane dateFilterPane;
    @FXML private Button deleteDateFilter;
    @FXML private Button dateFilterOption;
    @FXML private Pane nameFilterMenuPane;
    @FXML private Button nameFilterOption;
    @FXML private Pane nameFilterPane;
    @FXML private Button nameFilterButton;
    @FXML private Button deleteNameFilter;
    @FXML private TextField nameFilterText;
    @FXML private ImageView currentPagePhoto;
    @FXML private Label doneLabel;
    @FXML private Label doingLabel;
    @FXML private Label userTMS;
    @FXML private Button toDoDelete;
    @FXML private Button doingDelete;
    @FXML private Button doneDelete;
    @FXML private Button toDoAddButton;
    @FXML private Button doingAddButton;
    @FXML private Button doneAddButton;
    @FXML private VBox toDoCodeAreas;
    @FXML private VBox doingCodeAreas;
    @FXML private VBox doneCodeAreas;
    @FXML private Button toDoMenuButton;
    @FXML private Button doingMenuButton;
    @FXML private Button doneMenuButton;
    @FXML private Pane doneMenu;
    @FXML private VBox doneMenuVBox;
    @FXML private Pane doingMenu;
    @FXML private VBox doingMenuVBox;
    @FXML private Pane toDoMenu;
    @FXML private VBox toDoMenuVBox;
    @FXML private Button toDoCountButton;
    @FXML private Button doingCountButton;
    @FXML private Button doneCountButton;
    @FXML private VBox doneVBox;
    @FXML private VBox doingVBox;
    @FXML private VBox toDoVBox;
    @FXML private Pane doingPane;
    @FXML private Pane toDoPane;
    @FXML private Pane donePane;
    @FXML private CodeArea tasksCodeArea;
    @FXML private HBox taskStatusHBox;
    @FXML private HBox filterHBox;

    @FXML private Button filterButton;
    @FXML private Button sortButton;
    @FXML private Pane sortPane;

    @FXML private Label taskListPage;
    @FXML private TextField tasksTitle;
    @FXML private Pane tasksPage;
    @FXML private VBox sortVBox;

    @FXML private Label quickNotesPage;
    @FXML private VBox privateNamesBox;
    @FXML private VBox privateBox;
    @FXML private ScrollPane notesScroll;
    @FXML private Pane notePage;
    @FXML private HBox sampleHbox;
    @FXML private Label toDoLabel;
    ContextMenu openContextMenu = null;
    private int initialIndex;
    private double initialX;
    private double initialY;
    private HBox hboxBeingDragged;
    private int codeAreaInitialIndex;
    private CodeArea codeAreaBeingDragged;
    private double codeAreaInitialX;
    private double codeAreaInitialY;
    private double lastY = 0;

    private int i=0;
    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };
    private static final String[] DATA_TYPES = new String[] {
            "boolean", "byte", "char", "short", "int", "long",
            "float", "double", "void", "String"
    };
    private static final String WORD_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";

    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)
    private static final String HEX_COLOR_PATTERN = "\\b#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\\b";
    private static final String MENTION_PATTERN = "@\\w+";
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?\\b";
    CodeArea codeAreaFilePage = new CodeArea();
    Pattern pythonPattern = Pattern.compile("(?<KEYWORD>\\b(and|as|assert|async|await|break|class|continue|def|del|elif|else|except|False|finally|for|from|global|if|import|in|is|lambda|None|nonlocal|not|or|pass|raise|return|True|try|while|with|yield)\\b)|(?<PAREN>\\(|\\))|(?<BRACE>\\{|\\})|(?<BRACKET>\\[|\\])|(?<SEMICOLON>;)|(?<STRING>\"([^\"\\\\]|\\\\.)*\")|(?<COMMENT>#[^\\n]*)|(?<NUMBER>\\b\\d+(\\.\\d+)?\\b)|(?<HEX>\\b#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\\b)|(?<MENTION>@\\w+)");
    Pattern emptyPattern = Pattern.compile("^(?!x)x");
    MenuItem nameItem = new MenuItem("Start Task");
    MenuItem dateCreatedItem = new MenuItem("Complete Task");
    // Create the context menu and add the submenu to the Count button
    ContextMenu contextMenu = new ContextMenu();
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<HEX>" + HEX_COLOR_PATTERN + ")"
                    + "|(?<MENTION>" + MENTION_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
    );
    VisibleParagraphStyler pythonHighlighter = new VisibleParagraphStyler<>(codeAreaFilePage, text -> computeHighlighting(text, pythonPattern));
    VisibleParagraphStyler javaHighlighter = new VisibleParagraphStyler<>(codeAreaFilePage, text -> computeHighlighting(text, PATTERN));
    VisibleParagraphStyler noHighlighter = new VisibleParagraphStyler<>(codeAreaFilePage, text -> computeHighlighting(text, emptyPattern));
    VisibleParagraphStyler currentHighlighter = javaHighlighter;

    List<Integer> prevLengths = new ArrayList<>();
    Subscription javaSubscription = codeAreaFilePage.plainTextChanges()
            .subscribe(ignore -> codeAreaFilePage.setStyleSpans(0, computeHighlighting(codeAreaFilePage.getText(), PATTERN)));
    Subscription pythonSubscription = codeAreaFilePage.plainTextChanges()
            .subscribe(ignore -> codeAreaFilePage.setStyleSpans(0, computeHighlighting(codeAreaFilePage.getText(), pythonPattern)));
    Subscription noSubscription = codeAreaFilePage.plainTextChanges()
            .subscribe(ignore -> codeAreaFilePage.setStyleSpans(0, computeHighlighting(codeAreaFilePage.getText(), emptyPattern)));
    private static final String sampleCode = String.join("\n", new String[] {
            "Welcome to our task management software! We're glad you're here.\n" +
                    "\n" +
                    "Our software provides a powerful and intuitive platform for managing your tasks, projects, and deadlines. With our text editor, you'll be able to create and edit your tasks quickly and easily, and organize them in a way that works best for you.\n" +
                    "\n" +
                    "Whether you're a busy professional, a student, or just someone who needs to stay on top of their to-do list, our task management software is designed to help you be more productive and efficient.\n" +
                    "\n" +
                    "So, take some time to explore the features of our software and get started on your first task. If you have any questions or need assistance, our support team is always here to help.\n" +
                    "\n" +
                    "Thanks for choosing our task management software, and happy tasking!"
    });
    @FXML private JFXButton changeCover;
    @FXML private ScrollPane coverScroll;
    @FXML private  JFXButton share;
    @FXML private  VBox notesVbox;
    @FXML private Pane filePage;
    @FXML private TextField title;
    @FXML private JFXButton addCover;
    @FXML private JFXButton addComment;
    @FXML private JFXButton addIcon;
    @FXML private Label gettingStartedPage;
    @FXML private ImageView coverPhoto;
    @FXML private JFXButton repositionCover;
    @FXML private CodeArea sampleCodeArea;
    @FXML
    private static boolean isDone = true;
    @FXML private VBox titleVbox;
    @FXML private TextArea firstTextArea;
    @FXML private TextField sampleTextField;
    @FXML
    private VBox contentVbox;
    @FXML
    private AnchorPane menuBar;
    @FXML
    private ScrollPane scrollBar;
    @FXML
    private Tab customTab;
    @FXML
    private Label teamNameErrorLabel;
    @FXML
    private JFXButton uploadIconButton;
    @FXML
    private JFXButton submitLinkButton;
    @FXML
    private CustomTextField iconLink;
    @FXML
    private JFXButton plusButton;
    @FXML
    private Label charactersLeft;
    @FXML
    private TextArea teamDescription;
    @FXML
    private VBox teamNamesBox;
    @FXML
    private VBox teamNamesButtonBox;
    @FXML
    private VBox buttonNames;
    @FXML
    private HBox topBar;
    @FXML
    private HBox screenBox;
    @FXML
    private VBox workBox;
    private HBox controlsBox;
    @FXML
    private ImageView menuButtonImage;
    @FXML
    private javafx.scene.layout.StackPane StackPane;
    @FXML
    public TabPane TeamLogoPane;
    @FXML
    private JFXButton TeamIconButton;
    @FXML
    private ImageView TeamIcon;

    @FXML
    private Tab emojiTab;
    @FXML
    private TilePane emojiTile;
    @FXML
    private ScrollPane emojiScroll;

    @FXML
    private ScrollPane iconScroll;
    @FXML
    private Tab iconTab;
    @FXML
    private TilePane iconTile;

    @FXML
    private ImageView TeamImage;
    @FXML
    private ComboBox TeamType;
    @FXML
    JFXButton CreateTeam;
    @FXML
    CustomTextField TeamName;
    @FXML
    Pane TeamPane;
    @FXML
    private Pane HiderPane;
    @FXML
    JFXButton ManageTeams;
    @FXML
    private ImageView UserLogo;
    @FXML
    private ImageView UserLetter;
    @FXML
    private JFXButton LogOut;
    @FXML
    private JFXButton menuButton;
    @FXML
    private JFXButton closeMenuButton;
    @FXML
    private VBox leftVBox;
    @FXML
    VBox UserVBox;
    @FXML
    private JFXButton UserName;
    @FXML
    private Label UserEmail;
    @FXML
    static String email = "";
    double newWidth=955;
    static boolean UserInfoHidden = true;
    private static final String IMAGES_PATH = "file:./src/main/resources/tms/tms/images/";
    private static final String ICONS_PATH = "./src/main/resources/tms/tms/images/Map/";
    private static final String EMOJIS_PATH = "./src/main/resources/tms/tms/images/emojis/";
    private static final Map<String, Image> TEAM_IMAGES = new HashMap<>();
    static boolean fromExit = false;
    static String oldTeamName = "";
    private int previousLineCount;

    static {
        TEAM_IMAGES.put("        General team", new Image(IMAGES_PATH + "team.png"));
        TEAM_IMAGES.put("        Software engineering", new Image(IMAGES_PATH + "console.png"));
        TEAM_IMAGES.put("        Product", new Image(IMAGES_PATH + "product.png"));
        TEAM_IMAGES.put("        Sales", new Image(IMAGES_PATH + "sales.png"));
        TEAM_IMAGES.put("        Marketing", new Image(IMAGES_PATH + "marketing.png"));
    }

    private void fadeInAnimation(Node node, double time, double from, double to) {
        node.setVisible(true);
        node.setDisable(false);
        node.setMouseTransparent(false);
        FadeTransition nodeFadeIn = new FadeTransition(Duration.seconds(time), node);
        nodeFadeIn.setFromValue(from);
        nodeFadeIn.setToValue(to);
        nodeFadeIn.setOnFinished(d -> {
            node.setVisible(true);
            node.setOpacity(to);
        });
        nodeFadeIn.play();
    }

    private void fadeOutAnimation(Node node, double time, double from, double to) {
        node.setDisable(true);
        node.setMouseTransparent(true);
        FadeTransition nodeFadeOut = new FadeTransition(Duration.seconds(time), node);
        nodeFadeOut.setFromValue(from);
        nodeFadeOut.setToValue(to);
        nodeFadeOut.setOnFinished(c -> {
            node.setVisible(false);
            node.setOpacity(to);
        });
        nodeFadeOut.play();
    }

    public void toggleVBox(ActionEvent actionEvent) throws IOException {

        if (scrollBar.isVisible()) {
            fadeOutAnimation(closeMenuButton, 0.5, 1, 0);

            TranslateTransition buttonNamesTranslateDown = new TranslateTransition(BUTTONS_TRANS_DURATION, buttonNames);
            buttonNamesTranslateDown.setByY(60);

            TranslateTransition scrollBarTranslateDown = new TranslateTransition(BUTTONS_TRANS_DURATION, scrollBar);
            scrollBarTranslateDown.setByY(60);
            scrollBarTranslateDown.setOnFinished(a -> {
                TranslateTransition vboxTransitionLeft = new TranslateTransition(VBOX_TRANS_DURATION, workBox);
                vboxTransitionLeft.setByX(-scrollBar.getWidth());
                TranslateTransition filterButtonTransitionLeft = new TranslateTransition(VBOX_TRANS_DURATION, filterButton);
                filterButtonTransitionLeft.setByX(-scrollBar.getWidth());
                TranslateTransition sortButtonTransitionLeft = new TranslateTransition(VBOX_TRANS_DURATION, sortButton);
                sortButtonTransitionLeft.setByX(-scrollBar.getWidth());
                TranslateTransition buttonNamesTransitionLeft = new TranslateTransition(VBOX_TRANS_DURATION, buttonNames);
                buttonNamesTransitionLeft.setByX(-scrollBar.getWidth());

                Timeline timeline = new Timeline(
                        new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(topBar.prefWidthProperty(), 1532)),
                        new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(workBox.prefWidthProperty(), 1532))
                );

                TranslateTransition scrollBarTranslateLeft = new TranslateTransition(VBOX_TRANS_DURATION, scrollBar);
                scrollBarTranslateLeft.setByX(-scrollBar.getWidth());
                scrollBarTranslateLeft.setOnFinished(b -> {
                    scrollBar.setVisible(false);
                    fadeInAnimation(menuButton, 0.3, 0, 1);
                    topBar.prefWidthProperty().unbind();
                    topBar.setPrefWidth(1532);
                });
                scrollBarTranslateLeft.play();
                timeline.play();
                vboxTransitionLeft.play();
                buttonNamesTransitionLeft.play();
                filterButtonTransitionLeft.play();
                sortButtonTransitionLeft.play();

                HideUserVBox();
                UserInfoHidden = true;
            });
            buttonNamesTranslateDown.play();
            scrollBarTranslateDown.play();

            workBox.prefWidthProperty().unbind();
            filePage.setPrefWidth(1532);
            codeAreaFilePage.setPrefWidth(1532);

            workBox.setPrefWidth(1532);
            notesVbox.setMinWidth(1532);
            notesVbox.setMaxWidth(1532);
            notesScroll.setPrefWidth(1532);
            sampleCodeArea.setPrefWidth(1532);
            donePane.setLayoutX(donePane.getLayoutX()-scrollBar.getWidth());
            doingPane.setLayoutX(doingPane.getLayoutX()-scrollBar.getWidth());
            toDoPane.setLayoutX(toDoPane.getLayoutX()-scrollBar.getWidth());
            sortPane.setLayoutX(sortPane.getLayoutX()-scrollBar.getWidth());
            doneMenu.setLayoutX(doneMenu.getLayoutX()-scrollBar.getWidth());
            doingMenu.setLayoutX(doingMenu.getLayoutX()-scrollBar.getWidth());
            toDoMenu.setLayoutX(toDoMenu.getLayoutX()-scrollBar.getWidth());
            dateFilterOptionsPane.setLayoutX(dateFilterOptionsPane.getLayoutX()-scrollBar.getWidth());
            dateFilterMenuPane.setLayoutX(dateFilterMenuPane.getLayoutX()-scrollBar.getWidth());
            dateFilterPane.setLayoutX(dateFilterPane.getLayoutX()-scrollBar.getWidth());
            nameFilterMenuPane.setLayoutX(nameFilterMenuPane.getLayoutX()-scrollBar.getWidth());
            nameFilterPane.setLayoutX(nameFilterPane.getLayoutX()-scrollBar.getWidth());
        } else {
            scrollBar.setVisible(true);
            fadeOutAnimation(menuButton, 0.5, 1, 0);

            TranslateTransition vboxTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, workBox);
            vboxTransitionRight.setByX(scrollBar.getWidth());

            TranslateTransition buttonNamesTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, buttonNames);
            buttonNamesTransitionRight.setByX(scrollBar.getWidth());
            TranslateTransition filterButtonTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, filterButton);
            filterButtonTransitionRight.setByX(scrollBar.getWidth());
            TranslateTransition sortButtonTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, sortButton);
            sortButtonTransitionRight.setByX(scrollBar.getWidth());

            Timeline timeline = new Timeline(
                    new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(topBar.prefWidthProperty(), 1291)),
                    new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(workBox.prefWidthProperty(), 1291))
            );

            TranslateTransition scrollBarTranslateRight = new TranslateTransition(VBOX_TRANS_DURATION, scrollBar);
            scrollBarTranslateRight.setByX(scrollBar.getWidth());
            scrollBarTranslateRight.setOnFinished(a -> {
                TranslateTransition scrollBarTranslateUp = new TranslateTransition(BUTTONS_TRANS_DURATION, scrollBar);
                scrollBarTranslateUp.setByY(-60);
                TranslateTransition buttonNamesTranslateUp = new TranslateTransition(BUTTONS_TRANS_DURATION, buttonNames);
                buttonNamesTranslateUp.setByY(-60);
                scrollBarTranslateUp.setOnFinished(b -> {
                    fadeInAnimation(closeMenuButton, 0.5, 0, 1);
                    UserVBox.setDisable(false);
                    notesVbox.setMinWidth(1290);
                    notesVbox.setMaxWidth(1290);
                    topBar.prefWidthProperty().unbind();
                    topBar.setPrefWidth(1291);
                });
                scrollBarTranslateUp.play();
                buttonNamesTranslateUp.play();
            });
            scrollBarTranslateRight.play();
            vboxTransitionRight.play();
            filterButtonTransitionRight.play();
            sortButtonTransitionRight.play();
            buttonNamesTransitionRight.play();
            timeline.play();
            coverScroll.setPrefViewportWidth(1291);
            filePage.setPrefWidth(1300);
            codeAreaFilePage.setPrefWidth(1300);
            sampleCodeArea.setPrefWidth(1290);
            coverPhoto.setFitWidth(1291);
            workBox.prefWidthProperty().unbind();
            workBox.setPrefWidth(1291);
            donePane.setLayoutX(donePane.getLayoutX()+scrollBar.getWidth());
            doingPane.setLayoutX(doingPane.getLayoutX()+scrollBar.getWidth());
            toDoPane.setLayoutX(toDoPane.getLayoutX()+scrollBar.getWidth());
            sortPane.setLayoutX(sortPane.getLayoutX()+scrollBar.getWidth());
            doneMenu.setLayoutX(doneMenu.getLayoutX()+scrollBar.getWidth());
            doingMenu.setLayoutX(doingMenu.getLayoutX()+scrollBar.getWidth());
            toDoMenu.setLayoutX(toDoMenu.getLayoutX()+scrollBar.getWidth());

            dateFilterOptionsPane.setLayoutX(dateFilterOptionsPane.getLayoutX()+scrollBar.getWidth());
            dateFilterMenuPane.setLayoutX(dateFilterMenuPane.getLayoutX()+scrollBar.getWidth());
            dateFilterPane.setLayoutX(dateFilterPane.getLayoutX()+scrollBar.getWidth());
            nameFilterMenuPane.setLayoutX(nameFilterMenuPane.getLayoutX()+scrollBar.getWidth());
            nameFilterPane.setLayoutX(nameFilterPane.getLayoutX()+scrollBar.getWidth());
        }
    }

    public void toggleTeamsSettings() {

        if (leftVBox.isVisible() && buttonNames.isVisible()) {
            TranslateTransition leftVBoxTransitionLeft = new TranslateTransition( Duration.seconds(0.4), leftVBox);
            leftVBoxTransitionLeft.setByX(-scrollBar.getWidth());
            TranslateTransition buttonNamesTransitionLeft = new TranslateTransition( Duration.seconds(0.4), buttonNames);
            buttonNamesTransitionLeft.setByX(-scrollBar.getWidth());
            buttonNamesTransitionLeft.setOnFinished(b -> {
                leftVBox.setVisible(false);
                buttonNames.setVisible(false);
            });
            BrowseTeamSpaceVBox.setVisible(true);
            leftVBoxTransitionLeft.play();
            buttonNamesTransitionLeft.play();
            browseTeamsPane.setVisible(false);
        }
        else {
            TranslateTransition leftVBoxTransitionRight = new TranslateTransition( Duration.seconds(0.4), leftVBox);
            leftVBoxTransitionRight.setByX(scrollBar.getWidth());
            TranslateTransition buttonNamesTransitionRight = new TranslateTransition( Duration.seconds(0.4), buttonNames);
            buttonNamesTransitionRight.setByX(scrollBar.getWidth());
            leftVBoxTransitionRight.play();
            buttonNamesTransitionRight.play();
            buttonNamesTransitionRight.setOnFinished(e-> BrowseTeamSpaceVBox.setVisible(false));
            leftVBox.setVisible(true);
            buttonNames.setVisible(true);
            /*
            HideUserVBox();
            UserInfoHidden = true;
            });
            buttonNamesTranslateDown.play();
            scrollBarTranslateDown.play();

            workBox.prefWidthProperty().unbind();
            filePage.setPrefWidth(1532);
            codeAreaFilePage.setPrefWidth(1532);

            workBox.setPrefWidth(1532);
            notesVbox.setMinWidth(1532);
            notesVbox.setMaxWidth(1532);
            notesScroll.setPrefWidth(1532);
            sampleCodeArea.setPrefWidth(1532);
            donePane.setLayoutX(donePane.getLayoutX()-scrollBar.getWidth());
            doingPane.setLayoutX(doingPane.getLayoutX()-scrollBar.getWidth());
            toDoPane.setLayoutX(toDoPane.getLayoutX()-scrollBar.getWidth());
            sortPane.setLayoutX(sortPane.getLayoutX()-scrollBar.getWidth());
            doneMenu.setLayoutX(doneMenu.getLayoutX()-scrollBar.getWidth());
            doingMenu.setLayoutX(doingMenu.getLayoutX()-scrollBar.getWidth());
            toDoMenu.setLayoutX(toDoMenu.getLayoutX()-scrollBar.getWidth());
            dateFilterOptionsPane.setLayoutX(dateFilterOptionsPane.getLayoutX()-scrollBar.getWidth());
            dateFilterMenuPane.setLayoutX(dateFilterMenuPane.getLayoutX()-scrollBar.getWidth());
            dateFilterPane.setLayoutX(dateFilterPane.getLayoutX()-scrollBar.getWidth());
            nameFilterMenuPane.setLayoutX(nameFilterMenuPane.getLayoutX()-scrollBar.getWidth());
            nameFilterPane.setLayoutX(nameFilterPane.getLayoutX()-scrollBar.getWidth());
        } else {
            scrollBar.setVisible(true);
            fadeOutAnimation(menuButton, 0.5, 1, 0);

            TranslateTransition vboxTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, workBox);
            vboxTransitionRight.setByX(scrollBar.getWidth());

            TranslateTransition buttonNamesTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, buttonNames);
            buttonNamesTransitionRight.setByX(scrollBar.getWidth());
            TranslateTransition filterButtonTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, filterButton);
            filterButtonTransitionRight.setByX(scrollBar.getWidth());
            TranslateTransition sortButtonTransitionRight = new TranslateTransition(VBOX_TRANS_DURATION, sortButton);
            sortButtonTransitionRight.setByX(scrollBar.getWidth());

            Timeline timeline = new Timeline(
                    new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(topBar.prefWidthProperty(), 1291)),
                    new KeyFrame(VBOX_TRANS_DURATION, new KeyValue(workBox.prefWidthProperty(), 1291))
            );

            TranslateTransition scrollBarTranslateRight = new TranslateTransition(VBOX_TRANS_DURATION, scrollBar);
            scrollBarTranslateRight.setByX(scrollBar.getWidth());
            scrollBarTranslateRight.setOnFinished(a -> {
                TranslateTransition scrollBarTranslateUp = new TranslateTransition(BUTTONS_TRANS_DURATION, scrollBar);
                scrollBarTranslateUp.setByY(-60);
                TranslateTransition buttonNamesTranslateUp = new TranslateTransition(BUTTONS_TRANS_DURATION, buttonNames);
                buttonNamesTranslateUp.setByY(-60);
                scrollBarTranslateUp.setOnFinished(b -> {
                    fadeInAnimation(closeMenuButton, 0.5, 0, 1);
                    UserVBox.setDisable(false);
                    notesVbox.setMinWidth(1290);
                    notesVbox.setMaxWidth(1290);
                    topBar.prefWidthProperty().unbind();
                    topBar.setPrefWidth(1291);
                });
                scrollBarTranslateUp.play();
                buttonNamesTranslateUp.play();
            });
            scrollBarTranslateRight.play();
            vboxTransitionRight.play();
            filterButtonTransitionRight.play();
            sortButtonTransitionRight.play();
            buttonNamesTransitionRight.play();
            timeline.play();
            coverScroll.setPrefViewportWidth(1291);
            filePage.setPrefWidth(1300);
            codeAreaFilePage.setPrefWidth(1300);
            sampleCodeArea.setPrefWidth(1290);
            coverPhoto.setFitWidth(1291);
            workBox.prefWidthProperty().unbind();
            workBox.setPrefWidth(1291);
            donePane.setLayoutX(donePane.getLayoutX()+scrollBar.getWidth());
            doingPane.setLayoutX(doingPane.getLayoutX()+scrollBar.getWidth());
            toDoPane.setLayoutX(toDoPane.getLayoutX()+scrollBar.getWidth());
            sortPane.setLayoutX(sortPane.getLayoutX()+scrollBar.getWidth());
            doneMenu.setLayoutX(doneMenu.getLayoutX()+scrollBar.getWidth());
            doingMenu.setLayoutX(doingMenu.getLayoutX()+scrollBar.getWidth());
            toDoMenu.setLayoutX(toDoMenu.getLayoutX()+scrollBar.getWidth());

            dateFilterOptionsPane.setLayoutX(dateFilterOptionsPane.getLayoutX()+scrollBar.getWidth());
            dateFilterMenuPane.setLayoutX(dateFilterMenuPane.getLayoutX()+scrollBar.getWidth());
            dateFilterPane.setLayoutX(dateFilterPane.getLayoutX()+scrollBar.getWidth());
            nameFilterMenuPane.setLayoutX(nameFilterMenuPane.getLayoutX()+scrollBar.getWidth());
            nameFilterPane.setLayoutX(nameFilterPane.getLayoutX()+scrollBar.getWidth());
        }*/
        }
    }
    public void ShowUserInfo(ActionEvent actionEvent) throws IOException {
        UserName.setMouseTransparent(true);
        if (UserInfoHidden) {
            UserVBox.toFront();
            fadeInAnimation(UserVBox, 0.3, 0.7, 1);
            UserInfoHidden = false;
        } else {
            HideUserVBox();
            UserInfoHidden = true;
        }
        timeline(200, event -> UserName.setMouseTransparent(false)).play();
    }

    private Timeline timeline(double durationMillis, EventHandler<ActionEvent> eventHandler) {
        return new Timeline(new KeyFrame(Duration.millis(durationMillis), eventHandler));
    }

    public void HideUserVBox() {
        UserVBox.setOpacity(0);
        UserVBox.setVisible(true);
        UserVBox.toBack();
        UserVBox.setDisable(true);
    }

    @FXML
    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        JFXDialogLayout content = new JFXDialogLayout();
        Text heading = new Text(title);
        heading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        content.setHeading(heading);
        content.setBody(new Text(message));
        JFXButton okButton = new JFXButton("OK");
        JFXDialog alert = new JFXDialog(StackPane, content, JFXDialog.DialogTransition.CENTER);
        okButton.setOnAction(event -> {
            onConfirm.run();
            alert.close();
        });
        okButton.setStyle(STYLE_BUTTON_OK);

        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnAction(event -> alert.close());
        cancelButton.setStyle(STYLE_BUTTON_CANCEL);

        content.setActions(okButton, cancelButton);

        cancelButton.setCursor(Cursor.HAND);
        okButton.setCursor(Cursor.HAND);

        content.setStyle(STYLE_DIALOG_LAYOUT);

        alert.show();
    }

    @FXML
    void handleLogout() {
        String title = fromExit ? "Exit" : "Logout";
        String message = fromExit ? "Are you sure you want to quit? Any unsaved data will be lost." : "Are you sure you want to log out? Any unsaved data will be lost?";

        showConfirmationDialog(title, message, () -> {
            Platform.runLater(() -> {
                Stage currentStage = (Stage) LogOut.getScene().getWindow();
                currentStage.close();
                if (!fromExit) {
                    try {
                        new Main().start(new Stage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
    }
    private void populateTile(String path, TilePane tilePane, ImageView teamImage) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    Image image = new Image("file:" + file.getAbsolutePath());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);
                    Button button = new ImageButton(imageView);
                    button.setOnAction(event -> {
                        Image icon = new Image("file:" + file.getAbsolutePath());
                        teamImage.setImage(icon);
                    });
                    tilePane.setHgap(6);
                    tilePane.setVgap(7);
                    tilePane.getChildren().add(button);
                }
            }
        }
    }

    // Sets up the user dashboard section
    private void setUpUserDashboard() throws IOException {
        Stage window = (Stage) TeamPane.getScene().getWindow();
        window.setOnCloseRequest(event -> {
            event.consume(); // Prevent default close behavior
            fromExit = true;
            handleLogout();
        });
        //workBox.setStyle("-fx-border-style: solid; -fx-border-color: black");
        screenBox.setHgrow(workBox, Priority.ALWAYS);
        workBox.prefWidthProperty().bind(screenBox.widthProperty().subtract(scrollBar.widthProperty()));
        UserEmail.setText(email);

        String newName = User.getMember(email) + "'s TMS";
        UserName.setText(newName);
        userTMS.setText(newName);

        char firstChar = newName.charAt(0);
        String imagePath = "./src/main/resources/tms/tms/images/png/" + firstChar + ".png";
        try (InputStream inputStream = new FileInputStream(new File(imagePath))) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            UserLetter.setImage(image);
            UserLogo.setImage(image);
        }

        InitializeComboBox();
        HideUserVBox();
    }

    private void setTabContent(ScrollPane scrollPane, TilePane tilePane, Tab tab) {
        scrollPane.setContent(tilePane);
        tab.setContent(scrollPane);
    }

    void populateDashboard() throws IOException, SQLException {
        setUpUserDashboard();

        UserVBox.setDisable(false);
        UserVBox.setOpacity(0);
        menuButton.setVisible(false);
        menuButton.setOpacity(0);

        populateTile(ICONS_PATH, iconTile, TeamIcon);
        populateTile(EMOJIS_PATH, emojiTile, TeamIcon);

        setTabContent(iconScroll, iconTile, iconTab);
        setTabContent(emojiScroll, emojiTile, emojiTab);

        populateTile(ICONS_PATH, iconSettingsTile, teamSettingsTeamIcon);
        populateTile(EMOJIS_PATH, emojiSettingsTile, teamSettingsTeamIcon);

        setTabContent(iconSettingsScroll, iconSettingsTile, iconSettingsTab);
        setTabContent(emojiSettingsScroll, emojiSettingsTile, emojiSettingsTab);

        HiderPane.setVisible(false);
        TeamLogoPane.setVisible(false);
        TeamLogoPane.setOpacity(0);
    }

    private void addTeamNameValidator(CustomTextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = textField.getText().trim();
            if (!isValidTeamName(text) && text.length() == 0) errorLabel.setText("");
            else if (!isValidTeamName(text) && text.length() > 0) errorLabel.setText("Invalid team name");
            else {
                errorLabel.setText("");
            }
        });
    }

    public static boolean isValidTeamName(String teamName) {
        String regex = "^[A-Za-z0-9\\s]+$";
        return teamName.matches(regex);
    }

    public void toggleIconTile() {
        FadeTransition transition;
        if (!TeamLogoPane.isVisible()) {
            TeamLogoPane.setVisible(true);
            transition = new FadeTransition(Duration.seconds(0.3), TeamLogoPane);
            transition.setFromValue(0.0);
            transition.setToValue(1);
            transition.setOnFinished(finishedEvent -> {
                TeamLogoPane.setVisible(true);
                TeamLogoPane.setOpacity(1);
            });
        } else {
            transition = new FadeTransition(Duration.seconds(0.3), TeamLogoPane);
            transition.setFromValue(1);
            transition.setToValue(0.0);
            transition.setOnFinished(finishedEvent -> {
                TeamLogoPane.setVisible(false);
                TeamLogoPane.setOpacity(0);
            });
        }
        transition.play();
    }
    public void toggleTeamIconTile() {
        FadeTransition transition;
        if (!teamSettingsLogoPane.isVisible()) {
            teamSettingsLogoPane.setVisible(true);
            transition = new FadeTransition(Duration.seconds(0.3), teamSettingsLogoPane);
            transition.setFromValue(0.0);
            transition.setToValue(1);
            transition.setOnFinished(finishedEvent -> {
                teamSettingsLogoPane.setVisible(true);
                teamSettingsLogoPane.setOpacity(1);
            });
        } else {
            transition = new FadeTransition(Duration.seconds(0.3), teamSettingsLogoPane);
            transition.setFromValue(1);
            transition.setToValue(0.0);
            transition.setOnFinished(finishedEvent -> {
                teamSettingsLogoPane.setVisible(false);
                teamSettingsLogoPane.setOpacity(0);
            });
        }
        transition.play();
    }

    public void ChooseTeamType(ActionEvent actionEvent) {

    }

    public void closeTeamBox(ActionEvent actionEvent) {
        if (teamNamesButtonBox.isVisible()) {
            teamNamesBox.setVisible(false);
            teamNamesButtonBox.setVisible(false);
            teamNamesBox.setManaged(false);
            teamNamesButtonBox.setManaged(false);
        } else {
            teamNamesBox.setVisible(true);
            teamNamesButtonBox.setVisible(true);
            teamNamesBox.setManaged(true);
            teamNamesButtonBox.setManaged(true);
        }
    }
    public void submitSettingsLink(ActionEvent actionEvent) throws IOException {
        String urlText = iconSettingsLink.getText().trim();
        try {
            URL imageUrl = new URL(urlText);
            String imageName = generateRandomFileName();
            Path imagePath = Paths.get(IMAGES_PATH.substring(5), imageName);
            if (Files.notExists(imagePath)) {
                InputStream inputStream = imageUrl.openStream();
                Files.copy(inputStream, imagePath);
                inputStream.close();
            }
            Image image = new Image(imagePath.toUri().toString());
            teamSettingsTeamIcon.setImage(image);
        } catch (MalformedURLException e) {
            System.out.println("Bad URL: " + urlText);
        }
        iconSettingsLink.clear();
    }

    public void submitLink(ActionEvent actionEvent) throws IOException {
        String urlText = iconLink.getText().trim();
        try {
            URL imageUrl = new URL(urlText);
            String imageName = generateRandomFileName();
            Path imagePath = Paths.get(IMAGES_PATH.substring(5), imageName);
            if (Files.notExists(imagePath)) {
                InputStream inputStream = imageUrl.openStream();
                Files.copy(inputStream, imagePath);
                inputStream.close();
            }
            Image image = new Image(imagePath.toUri().toString());
            TeamIcon.setImage(image);
        } catch (MalformedURLException e) {
            System.out.println("Bad URL: " + urlText);
        }
        iconLink.clear();
    }

    private String generateRandomFileName() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        sb.append(".png");
        return sb.toString();
    }

    public void uploadSettingsIcon(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.ico", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(uploadIconButton.getScene().getWindow());
        if (selectedFile != null) {
            String extension = getFileExtension(selectedFile);
            if (extension != null && (extension.equals("ico") || extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                Image icon = new Image("file:" + selectedFile.getAbsolutePath());
                TeamIcon.setImage(icon);
            } else {
                System.out.println("Selected file is not a valid image.");
            }
        }
    }
    public void uploadIcon(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.ico", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(uploadSettingsIconButton.getScene().getWindow());
        if (selectedFile != null) {
            String extension = getFileExtension(selectedFile);
            if (extension != null && (extension.equals("ico") || extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                Image icon = new Image("file:" + selectedFile.getAbsolutePath());
                teamSettingsTeamIcon.setImage(icon);
            } else {
                System.out.println("Selected file is not a valid image.");
            }
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    public void closePrivateBox(ActionEvent actionEvent) {
        if (privateBox.isVisible()){
            privateBox.setVisible(false);
            privateBox.setManaged(false);
            privateNamesBox.setVisible(false);
            privateNamesBox.setManaged(false);
        }
        else {
            privateBox.setVisible(true);
            privateBox.setManaged(true);
            privateNamesBox.setVisible(true);
            privateNamesBox.setManaged(true);
        }
    }

    public void addCoverPhoto(ActionEvent actionEvent) {
        if (addCover.getText().equals("Add cover")) {
            coverScroll.setManaged(true);
            coverScroll.setVisible(true);
            coverScroll.setPrefViewportWidth(1532);
            coverScroll.setPrefViewportHeight(200);
            coverPhoto.setFitWidth(1532);
            coverPhoto.setPreserveRatio(true);
            addCover.setPrefWidth(113);
            addCover.setText("Remove cover");
            repositionCover.setVisible(true);
            changeCover.setVisible(true);
            notesVbox.setPrefHeight(470);
            notesScroll.setPrefHeight(470);
            donePane.setLayoutY(donePane.getLayoutY()+200);
            doingPane.setLayoutY(doingPane.getLayoutY()+200);
            toDoPane.setLayoutY(toDoPane.getLayoutY()+200);
            filterButton.setLayoutY(filterButton.getLayoutY()+200);
            sortButton.setLayoutY(sortButton.getLayoutY()+200);
            sortPane.setLayoutY(sortPane.getLayoutY()+200);
            doneMenu.setLayoutY(doneMenu.getLayoutY()+200);
            doingMenu.setLayoutY(doingMenu.getLayoutY()+200);
            toDoMenu.setLayoutY(toDoMenu.getLayoutY()+200);
        }
        else {
            coverScroll.setManaged(false);
            coverScroll.setVisible(false);
            addCover.setText("Add cover");
            addCover.setPrefWidth(97);
            repositionCover.setVisible(false);
            changeCover.setVisible(false);
            notesVbox.setPrefHeight(670);
            notesScroll.setPrefHeight(670);
            donePane.setLayoutY(donePane.getLayoutY()-200);
            doingPane.setLayoutY(doingPane.getLayoutY()-200);
            toDoPane.setLayoutY(toDoPane.getLayoutY()-200);
            filterButton.setLayoutY(filterButton.getLayoutY()-200);
            sortButton.setLayoutY(sortButton.getLayoutY()-200);
            sortPane.setLayoutY(sortPane.getLayoutY()-200);
            doneMenu.setLayoutY(doneMenu.getLayoutY()-200);
            doingMenu.setLayoutY(doingMenu.getLayoutY()-200);
            toDoMenu.setLayoutY(toDoMenu.getLayoutY()-200);
        }
    }

    public void repositionCoverPhoto(ActionEvent actionEvent) {
        if (coverScroll.mouseTransparentProperty().getValue()) {
            coverScroll.setMouseTransparent(false);
            coverScroll.setCursor(Cursor.MOVE);
            repositionCover.setText("Save position");
            repositionCover.setPrefWidth(87);
        }
        else {
            coverScroll.setMouseTransparent(true);
            coverScroll.setCursor(Cursor.DEFAULT);
            repositionCover.setText("Reposition");
            repositionCover.setPrefWidth(75);
        }
    }

    public void changeCoverPhoto(ActionEvent actionEvent) {
        File dir = new File("./src/main/resources/tms/tms/images/cover/");
        File[] files = dir.listFiles();
        List<File> imageFiles = Arrays.stream(files)
                .filter(f -> f.isFile() && f.getName().endsWith(".jpg"))
                .collect(Collectors.toList());
        File selectedFile = imageFiles.get((int)(Math.random() * imageFiles.size()));
        Image image = new Image(selectedFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1532);
        imageView.setPreserveRatio(true);
        coverScroll.setContent(imageView);
    }

    public void changeIcon(ActionEvent actionEvent) {
    }
    private ChangeListener<String> pageChangeListener = (observable, oldValue, newValue) -> {
        if (notePage.isVisible()) quickNotesPage.setText(newValue);
        else if (filePage.isVisible()) gettingStartedPage.setText(newValue);
        else if (tasksPage.isVisible()) taskListPage.setText(newValue);
    };
    void setHighlighter(VisibleParagraphStyler highlighter) {
        noSubscription.unsubscribe();
        javaSubscription.unsubscribe();
        pythonSubscription.unsubscribe();
        codeAreaFilePage.getVisibleParagraphs().removeModificationObserver(currentHighlighter);
        currentHighlighter = highlighter;
        codeAreaFilePage.getVisibleParagraphs().addModificationObserver(highlighter);
        String text = codeAreaFilePage.getText();
        codeAreaFilePage.clear();
        codeAreaFilePage.replaceText(text);
    }
    public void showFilePage(ActionEvent actionEvent) {
        Image image = new Image(IMAGES_PATH + "play.png");
        currentPagePhoto.setImage(image);
        doingPane.setVisible(false);
        toDoPane.setVisible(false);
        donePane.setVisible(false);
        doneMenu.setVisible(false);
        doingMenu.setVisible(false);
        toDoMenu.setVisible(false);
        controlsBox.setVisible(true);
        controlsBox.setManaged(true);
        filterButton.setVisible(false);
        sortButton.setVisible(false);
        sortPane.setVisible(false);
        notePage.setVisible(false);
        notePage.setManaged(false);
        tasksPage.setManaged(false);
        tasksPage.setVisible(false);
        filePage.setManaged(true);
        filePage.setVisible(true);
        title.setText(gettingStartedPage.getText());
        title.textProperty().removeListener(pageChangeListener);
        title.textProperty().addListener(pageChangeListener);
    }

    public void showNotePage(ActionEvent actionEvent) {
        Image image = new Image(IMAGES_PATH + "pin.png");
        currentPagePhoto.setImage(image);
        doingPane.setVisible(false);
        toDoPane.setVisible(false);
        donePane.setVisible(false);
        controlsBox.setVisible(false);
        controlsBox.setManaged(false);
        filterButton.setVisible(false);
        sortButton.setVisible(false);
        sortPane.setVisible(false);
        filePage.setManaged(false);
        filePage.setVisible(false);
        tasksPage.setManaged(false);
        tasksPage.setVisible(false);
        notePage.setVisible(true);
        notePage.setManaged(true);
        title.setText(quickNotesPage.getText());
        title.textProperty().removeListener(pageChangeListener);
        title.textProperty().addListener(pageChangeListener);
    }

    public void toggleFilter(ActionEvent actionEvent) {
        if (!filterHBox.isVisible()) {
            filterHBox.setManaged(true);
            filterHBox.setVisible(true);
            donePane.setLayoutY(donePane.getLayoutY()+filterHBox.getHeight());
            doingPane.setLayoutY(doingPane.getLayoutY()+filterHBox.getHeight());
            toDoPane.setLayoutY(toDoPane.getLayoutY()+filterHBox.getHeight());
            doneMenu.setLayoutY(doneMenu.getLayoutY()+filterHBox.getHeight());
            doingMenu.setLayoutY(doingMenu.getLayoutY()+filterHBox.getHeight());
            toDoMenu.setLayoutY(toDoMenu.getLayoutY()+filterHBox.getHeight());
        }
        else {
            filterHBox.setManaged(false);
            filterHBox.setVisible(false);
            donePane.setLayoutY(donePane.getLayoutY()-filterHBox.getHeight());
            doingPane.setLayoutY(doingPane.getLayoutY()-filterHBox.getHeight());
            toDoPane.setLayoutY(toDoPane.getLayoutY()-filterHBox.getHeight());
            doneMenu.setLayoutY(doneMenu.getLayoutY()-filterHBox.getHeight());
            doingMenu.setLayoutY(doingMenu.getLayoutY()-filterHBox.getHeight());
            toDoMenu.setLayoutY(toDoMenu.getLayoutY()-filterHBox.getHeight());
        }
    }

    public void toggleSort() {
        FadeTransition transition;
        if (!sortPane.isVisible()) {
            sortPane.setVisible(true);
            transition = new FadeTransition(Duration.seconds(0.3), sortPane);
            transition.setFromValue(0.0);
            transition.setToValue(1);
            transition.setOnFinished(finishedEvent -> {
                sortPane.setVisible(true);
                sortPane.setOpacity(1);
            });
            transition.play();
        } else {
            sortPane.setVisible(false);
        }
    }
    private void prepareCountFunctions (VBox vbox) {
        for (Node node : vbox.getChildren()) {
            if (node instanceof Button) {
                if (Objects.equals(((Button) node).getId(), null)) {
                    ContextMenu contextMenu = ((Button) node).getContextMenu();
                    node.setOnMouseClicked(event -> {
                        if (openContextMenu != null && openContextMenu.isShowing()) {
                            openContextMenu.hide();
                        }
                        contextMenu.show(node, event.getScreenX(), event.getScreenY());
                        openContextMenu = contextMenu;
                    });
                }
            }
        }
    }

    public void leaveTeam(ActionEvent actionEvent) {
    }

    public void archiveTeam(ActionEvent actionEvent) {
    }

    private class ImageButton extends Button {
        public ImageButton(ImageView imageView) {
            setGraphic(imageView);
            setPrefWidth(30);
            setPrefHeight(30);
            setMaxWidth(30);
            setMaxHeight(30);
            setStyle("-fx-background-color: transparent;");
            setOnMouseEntered(event -> setStyle("-fx-background-color: #d7d7d7;"));
            setOnMousePressed(event -> setStyle("-fx-background-color: #a7a7a7;"));
            setOnMouseReleased(event -> setStyle("-fx-background-color: #d7d7d7;"));
            setOnMouseExited(event -> setStyle("-fx-background-color: transparent;"));
            setCursor(Cursor.HAND);
        }
    }

    public void MouseHide(MouseEvent e) {
        if (UserVBox.getOpacity() == 1) {
            if (!UserVBox.getBoundsInParent().contains(e.getX(), e.getY()) && !UserName.getBoundsInParent().contains(e.getX(), e.getY())) {
                HideUserVBox();
                UserInfoHidden = true;
            }
        }
    }
    private void setButtonState(boolean isDisabled, Button button) {
        if (isDisabled) {
            button.setStyle("-fx-background-color: #b3cdf1; -fx-text-fill: #ffffff;");
            button.setDisable(true);
            button.setCursor(Cursor.DEFAULT);
        } else {
            button.setDisable(false);
            button.setStyle("-fx-background-color: #4281db; -fx-text-fill: #ffffff;");
            button.setCursor(Cursor.HAND);
        }
    }
    private void changeMenuButtonImage(String imageName) {
        Image image = new Image(IMAGES_PATH + imageName);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(menuButton.getWidth());
        imageView.setFitHeight(menuButton.getHeight());
        menuButtonImage.setOpacity(0);
        menuButtonImage.setImage(imageView.getImage());
        FadeTransition fade = new FadeTransition(Duration.seconds(0.2), menuButtonImage);
        fade.setToValue(1);
        fade.play();
    }
    private StyleSpans<Collection<String>> computeHighlighting(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                                    matcher.group("HEX") != null ? "hex" :
                                                                                            matcher.group("MENTION") != null ? "mention" :
                                                                                                    matcher.group("NUMBER") != null ? "number" :
                                                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }
    private class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String,StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler( GenericStyledArea<PS, SEG, S> area, Function<String,StyleSpans<S>> computeStyles )
        {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept( ListModification<? extends Paragraph<PS, SEG, S>> lm )
        {
            if ( lm.getAddedSize() > 0 ) Platform.runLater( () ->
            {
                int paragraph = Math.min( area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size()-1 );
                String text = area.getText( paragraph, 0, paragraph, area.getParagraphLength( paragraph ) );

                if ( paragraph != prevParagraph || text.length() != prevTextLength )
                {
                    if ( paragraph < area.getParagraphs().size()-1 )
                    {
                        int startPos = area.getAbsolutePosition( paragraph, 0 );
                        area.setStyleSpans( startPos, computeStyles.apply( text ) );
                    }
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            });
        }
    }
    private class DefaultContextMenu extends ContextMenu {
        private MenuItem fold, unfold, print;

        public DefaultContextMenu()
        {
            fold = new MenuItem( "Fold selected text" );
            fold.setOnAction( AE -> { hide(); fold(); } );

            unfold = new MenuItem( "Unfold from cursor" );
            unfold.setOnAction( AE -> { hide(); unfold(); } );

            print = new MenuItem( "Print" );
            print.setOnAction( AE -> { hide(); print(); } );

            getItems().addAll( fold, unfold, print );
        }

        /**
         * Folds multiple lines of selected text, only showing the first line and hiding the rest.
         */
        private void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }

        /**
         * Unfold the CURRENT line/paragraph if it has a fold.
         */
        private void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs( area.getCurrentParagraph() );
        }

        private void print() {
            System.out.println( ((CodeArea) getOwnerNode()).getText() );
        }
    }
    private Button createButton(CodeArea area, String styleClass, String toolTip) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setPrefWidth(25);
        button.setPrefHeight(25);
        if (toolTip != null) {
            button.setTooltip(new Tooltip(toolTip));
        }
        return button;
    }
    private void setMouseEnteredEvents(Node node) {
        node.setOnMouseEntered(event -> {
            addCover.setVisible(node == titleVbox);
            changeCover.setOpacity(1);
            repositionCover.setOpacity(1);
        });
        node.setOnMouseExited(event -> {
            addCover.setVisible(false);
            changeCover.setOpacity(0);
            repositionCover.setOpacity(0);
        });
    }
    private void setUpTitlePage() {
        setMouseEnteredEvents(changeCover);
        setMouseEnteredEvents(repositionCover);
        setMouseEnteredEvents(titleVbox);
    }
    @FXML
    private void setUpFilePage() {
        codeAreaFilePage.setWrapText(true);
        codeAreaFilePage.setStyle("-fx-fill: #af43f5; -fx-font-family: System; -fx-font-size: 16;-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0");
        VBox.setVgrow(codeAreaFilePage, Priority.ALWAYS);
        // Get the font from the CodeArea's style
        codeAreaFilePage.setId("codeArea");
        filePage.setPrefWidth(1290);
        codeAreaFilePage.setPrefHeight(670);
        codeAreaFilePage.setPrefWidth(1290);
        codeAreaFilePage.setWrapText(true);
        codeAreaFilePage.setStyle("-fx-font-family: \"Roboto Light\"; -fx-font-size: 20; -fx-background-color: #ffffff");
        codeAreaFilePage.setParagraphGraphicFactory(LineNumberFactory.get(codeAreaFilePage));
        codeAreaFilePage.setContextMenu( new DefaultContextMenu() );
        codeAreaFilePage.getVisibleParagraphs().addModificationObserver(javaHighlighter);
        codeAreaFilePage.clear();
        codeAreaFilePage.replaceText(0, 0, sampleCode);
        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeAreaFilePage.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeAreaFilePage.getCaretPosition();
                int currentParagraph = codeAreaFilePage.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeAreaFilePage.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeAreaFilePage.insertText( caretPosition, m0.group() ) );
            }
        });
// Create a ChoiceBox for font selection
        ComboBox<String> editorChoice = new ComboBox<>();
        editorChoice.getItems().addAll("Java","Python","No text highlighting");
        editorChoice.setValue("Java");
        editorChoice.setOnAction(e -> {
            String selectedValue = editorChoice.getValue();
            if (selectedValue.equals("Java")) {
                setHighlighter(javaHighlighter);
            } else if (selectedValue.equals("Python")) {
                setHighlighter(pythonHighlighter);
            }
            else {
                setHighlighter(noHighlighter);
            }
        });

        ComboBox<String> fontChoiceBox = new ComboBox<>();
        List<String> systemFonts = Font.getFamilies();
        fontChoiceBox.getItems().addAll(systemFonts);
        fontChoiceBox.getItems().add("Roboto Light");
        fontChoiceBox.setValue("Roboto Light");
        fontChoiceBox.setId("fontBox");
// Create a ChoiceBox for font size selection
        ComboBox<String> fontSizeChoiceBox = new ComboBox<>();
        fontSizeChoiceBox.getItems().addAll("5","6","7","8","9","10","12",
                "14", "16", "18","20","24","28","32","36","40","48","56","64","72");
        fontSizeChoiceBox.setValue("20");
        fontSizeChoiceBox.setId("fontSizeBox");
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
// Create a Button for applying the selected font and font size
        Button applyButton = createButton(codeAreaFilePage,"apply","Apply");
        Button saveButton = createButton(codeAreaFilePage,"save","Save");
        Button loadButton =createButton(codeAreaFilePage,"load","Load");
        Button undoBtn = createButton(codeAreaFilePage,"undo", "Undo");
        Button redoBtn = createButton(codeAreaFilePage,"redo", "Redo");
        Button cutBtn = createButton(codeAreaFilePage,"cut", "Cut");
        Button copyBtn = createButton(codeAreaFilePage,"copy", "Copy");
        Button pasteBtn = createButton(codeAreaFilePage,"paste", "Paste");

        undoBtn.disableProperty().bind(codeAreaFilePage.undoAvailableProperty().map(x -> !x));
        redoBtn.disableProperty().bind(codeAreaFilePage.redoAvailableProperty().map(x -> !x));

        BooleanBinding selectionEmpty = new BooleanBinding() {
            { bind(codeAreaFilePage.selectionProperty()); }

            @Override
            protected boolean computeValue() {
                return codeAreaFilePage.getSelection().getLength() == 0;
            }
        };
        cutBtn.disableProperty().bind(selectionEmpty);
        copyBtn.disableProperty().bind(selectionEmpty);

        ToolBar toolBar = new ToolBar(
                loadButton, saveButton,  new Separator(Orientation.VERTICAL),
                undoBtn, redoBtn, new Separator(Orientation.VERTICAL),
                cutBtn, copyBtn, pasteBtn);
        toolBar.setMinHeight(30);

        codeAreaFilePage.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                saveButton.fire(); // trigger the saveButton's action
                event.consume(); // consume the event to prevent further propagation
            }
        });
        undoBtn.setOnAction(event -> codeAreaFilePage.undo());
        redoBtn.setOnAction(event -> codeAreaFilePage.redo());
        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            // Add filters for .txt, .py and .java file extensions
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text documents (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("Python files (*.py)", "*.py"),
                    new FileChooser.ExtensionFilter("Java files (*.java)", "*.java")
            );
            File selectedFile = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
            if (selectedFile != null) {
                String extension = getFileExtension(selectedFile);
                if (extension != null && List.of("txt", "py", "java").contains(extension)) {
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    try (FileWriter writer = new FileWriter(selectedFile);
                         BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                        bufferedWriter.write(codeAreaFilePage.getText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // The file does not have a valid extension
                    System.out.println("Selected file is not a valid file.");
                }
            }
        });

        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Load document", "*.txt", "*.py", "*.java")
            );
            File selectedFile = fileChooser.showOpenDialog(loadButton.getScene().getWindow());
            if (selectedFile != null) {
                String extension = getFileExtension(selectedFile);
                if (extension != null && List.of("txt", "py", "java").contains(extension)) {
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    try (FileReader reader = new FileReader(selectedFile);
                         BufferedReader bufferedReader = new BufferedReader(reader)) {
                        StringBuilder text = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            text.append(line).append("\n");
                        }
                        // Set the text of the CodeArea
                        codeAreaFilePage.replaceText(text.toString());
                        // Upload the file or do any other necessary handling
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // The file does not have a valid image extension
                    System.out.println("Selected file is not a valid text file.");
                }
            }
        });
        copyBtn.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(codeAreaFilePage.getSelectedText());
            clipboard.setContent(content);
        });
        cutBtn.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(codeAreaFilePage.getSelectedText());
            clipboard.setContent(content);
            codeAreaFilePage.deleteText(codeAreaFilePage.getSelection());
        });
        pasteBtn.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            String content = clipboard.getString();
            codeAreaFilePage.insertText(codeAreaFilePage.getCaretPosition(), content);
        });
        controlsBox = new HBox(10, fontChoiceBox, fontSizeChoiceBox, toolBar, colorPicker, editorChoice, applyButton);
        controlsBox.setPrefWidth(1550);
        applyButton.setOnAction(event -> {
            String fontName = fontChoiceBox.getValue();
            String fontSize = fontSizeChoiceBox.getValue();
            String color = colorPicker.getValue().toString().replace("0x", "#");
            codeAreaFilePage.setStyle("-fx-font-family: " + fontName + "; -fx-font-size: " + fontSize +";");

            Platform.runLater(() -> {
                Scene scene = codeAreaFilePage.getScene();
                if (scene == null) return;

                Parent root = scene.getRoot();
                if (root == null) return;

                root.lookupAll(".paragraph-text .text")
                        .forEach(node -> node.setStyle("-fx-fill: " + color + ";"));
            });
        });

// Add the controls to a HBox
// Add the controls and the CodeArea to a VBox
        VirtualizedScrollPane scroll = new VirtualizedScrollPane(codeAreaFilePage);
        int index = contentVbox.getChildren().indexOf(titleVbox);
        contentVbox.getChildren().add(index+1, controlsBox);
        filePage.getChildren().add(scroll);
    }
    private void setUpHBoxMouseEvents(HBox hbox, Button dragButton, Button addButton) {
        hbox.setOnMouseEntered(event -> {
                dragButton.setVisible(true);
                addButton.setVisible(true);
        });

        hbox.setOnMouseExited(event -> {
            dragButton.setVisible(false);
            addButton.setVisible(false);
        });
    }

    public void showTaskPage(ActionEvent actionEvent) {
        Image image = new Image(IMAGES_PATH + "scissors.png");
        currentPagePhoto.setImage(image);
        controlsBox.setVisible(false);
        controlsBox.setManaged(false);
        filePage.setManaged(false);
        filePage.setVisible(false);
        notePage.setVisible(false);
        notePage.setManaged(false);
        tasksPage.setManaged(true);
        tasksPage.setVisible(true);
        filterButton.setVisible(true);
        filterButton.setManaged(true);
        sortButton.setVisible(true);
        sortButton.setManaged(true);
        sortPane.setVisible(true);
        sortPane.setManaged(true);
        prepareCountFunctions(toDoVBox);
        prepareCountFunctions(doingVBox);
        prepareCountFunctions(doneVBox);
        title.setText(taskListPage.getText());
        title.textProperty().removeListener(pageChangeListener);
        title.textProperty().addListener(pageChangeListener);
    }
    private void setUpTasks(VBox parentVBox) {
        CodeArea codeArea = new CodeArea();
        codeArea.replaceText(tasksCodeArea.getText());
        codeArea.setEffect(tasksCodeArea.getEffect());
        codeArea.setAutoHeight(true);
        codeArea.setWrapText(true);
        codeArea.setStyle(tasksCodeArea.getStyle());
        codeArea.setPrefWidth(tasksCodeArea.getPrefWidth());
        codeArea.setPrefHeight(tasksCodeArea.getPrefHeight());
        codeArea.setMinWidth(tasksCodeArea.getMinWidth());
        codeArea.setMinHeight(tasksCodeArea.getMinHeight());
        codeArea.setPadding(tasksCodeArea.getPadding());
        setMargin(codeArea,new Insets(0,3,0,14));
        codeArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && (codeArea.getText().isBlank() || codeArea.getText().equals("Untitled"))) {
                codeArea.replaceText("Untitled");
                Platform.runLater(() -> {
                    codeArea.selectLine();
                });
            }
            else{
                codeArea.deselect();
            }
        });
        codeArea.setOnKeyTyped(e -> {
            // Remove the prompt text if the user types into the control
            if ("Untitled".equals(codeArea.getText())) {
                codeArea.replaceText("");
            }
        });

        MenuItem nameItem = new MenuItem("Start Task");
        nameItem.setOnAction(event -> {
            System.out.println(codeArea.getText());
        });
        MenuItem dateCreatedItem = new MenuItem("Complete Task");
        dateCreatedItem.setOnAction(event -> {
            // perform action when "Complete Task" is selected
        });
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(nameItem, dateCreatedItem);
        codeArea.setContextMenu(contextMenu);
        codeArea.setOnContextMenuRequested(event -> {
            contextMenu.show(codeArea, event.getScreenX(), event.getScreenY());
        });
        codeArea.setOnMousePressed(event -> {
            codeAreaInitialIndex = parentVBox.getChildren().indexOf(codeArea);
            codeAreaInitialX = event.getSceneX();
            codeAreaInitialY = event.getSceneY();
            codeAreaBeingDragged = codeArea;
        });
        codeArea.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - codeAreaInitialX;
            double offsetY = event.getSceneY() - codeAreaInitialY;
            codeArea.setLayoutX(codeArea.getLayoutX() + offsetX);
            codeArea.setLayoutY(codeArea.getLayoutY() + offsetY);
            codeAreaInitialX = event.getSceneX();
            codeAreaInitialY = event.getSceneY();
            int newIndex = 0;
            for (int i = 0; i < parentVBox.getChildren().size(); i++) {
                Bounds bounds = parentVBox.getChildren().get(i).localToScene(parentVBox.getChildren().get(i).getBoundsInLocal());
                if (event.getSceneY() > bounds.getMaxY()) {
                    newIndex = i + 1;
                } else {
                    break;
                }
            }
            if (newIndex != codeAreaInitialIndex && codeAreaInitialIndex != parentVBox.getChildren().size() - 1) {
                parentVBox.getChildren().remove(codeArea);
                parentVBox.getChildren().add(newIndex, codeArea);
                codeAreaInitialIndex = newIndex;
            }
        });
        codeArea.setOnMouseReleased(event -> {
            codeArea.setLayoutX(0);
            codeArea.setLayoutY(0);
            parentVBox.getChildren().remove(codeAreaBeingDragged);
            parentVBox.getChildren().add(codeAreaInitialIndex, codeAreaBeingDragged);
            codeAreaBeingDragged = null;
        });

        parentVBox.getChildren().add(0,codeArea);
        parentVBox.layout();
    }
    private void setUpNotes(HBox parentHBox) {
        CodeArea codeArea = new CodeArea();
        codeArea.setWrapText(true);
        codeArea.setStyle("-fx-fill: #af43f5; -fx-font-family: System; -fx-font-size: 16;-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0");
        VBox.setVgrow(codeArea, Priority.ALWAYS);
        // Get the font from the CodeArea's style
        Font font = Font.font("System",21);
// Create a Text object to measure the height of the text
        Text text = new Text();
        text.setFont(font);
        codeArea.setId("codeArea"+i);
// Bind the height of the Text object to the width and text of the CodeArea
        DoubleBinding textHeight = Bindings.createDoubleBinding(() -> {
            text.setText(codeArea.getText());
            double textWidth = codeArea.getWidth() - codeArea.getPadding().getLeft() - codeArea.getPadding().getRight();
            System.out.println(text.getLayoutBounds().getHeight()+","+codeArea.getWidth());
            text.setWrappingWidth(textWidth);

            return text.getLayoutBounds().getHeight() + codeArea.getPadding().getTop() + codeArea.getPadding().getBottom();
        }, codeArea.textProperty(), codeArea.widthProperty());

        HBox hbox = new HBox();
        textHeight.addListener((obs, oldHeight, newHeight) -> {
            if (newHeight.intValue() < 30) {
                codeArea.minHeightProperty().unbind();
                hbox.minHeightProperty().unbind();
                codeArea.setMinHeight(30);
                hbox.setMinHeight(30);
            } else {
                codeArea.minHeightProperty().bind(textHeight);
            }
        });
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            double newHeight = textHeight.get();
            if (newHeight < 30) {
                codeArea.minHeightProperty().unbind();
                hbox.minHeightProperty().unbind();
                codeArea.setMinHeight(30);
                hbox.setMinHeight(30);
            } else {
                codeArea.minHeightProperty().bind(textHeight);
                hbox.minHeightProperty().bind(textHeight);
            }
        });
// Bind the minHeight of the HBox containing the CodeArea to the height of the CodeArea
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);
        setMargin(codeArea, new Insets(50, 10, 50, 0));
        hbox.setPadding(new Insets(0,0,0,60));
        hbox.setStyle("-fx-background-color: #ffffff;");
        HBox.setHgrow(codeArea, Priority.ALWAYS);

        Button dragButton = new Button("");
        dragButton.setStyle("-fx-background-color: transparent");
        dragButton.setCursor(Cursor.CLOSED_HAND);
        dragButton.setPrefHeight(20);
        dragButton.setPrefWidth(20);
        dragButton.setPadding(new Insets(1,0,0,0));
        dragButton.setStyle("-fx-background-color: transparent");
        dragButton.setCursor(Cursor.CLOSED_HAND);
        dragButton.setVisible(false);
        Image dragImage = new Image(IMAGES_PATH+"drag.png");
        ImageView dragImageView = new ImageView(dragImage);
        dragButton.setGraphic(dragImageView);

        Button addButton = new Button("");
        addButton.setPrefHeight(20);
        addButton.setPrefWidth(20);
        addButton.setPadding(new Insets(1,0,0,0));
        addButton.setStyle("-fx-background-color: transparent");
        addButton.setCursor(Cursor.HAND);
        addButton.setVisible(false);
        Image addImage = new Image(IMAGES_PATH+"plus.png");
        ImageView addImageView = new ImageView(addImage);
        addButton.setGraphic(addImageView);
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #eeeeee"));
        addButton.setOnMousePressed(e -> addButton.setStyle("-fx-background-color: #cccccc"));
        addButton.setOnMouseReleased(e -> addButton.setStyle("-fx-background-color: transparent"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: transparent"));

        addButton.setOnAction(e -> {
            Node addButtonNode = addButton.getParent(); // Get the parent node of the addButton
            while (!(addButtonNode instanceof HBox)) { // Find the HBox containing the addButton
                addButtonNode = addButtonNode.getParent();
            }
            setUpNotes((HBox) addButtonNode);
        });

        JFXCheckBox checkNote = new JFXCheckBox("");
        int finalI = i;
        codeArea.textProperty().addListener((observable, oldValue, newValue) -> {
            String textWithoutNewlines = newValue.replaceAll("\n", " ");
            codeArea.replaceText(textWithoutNewlines);
        });

        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
        checkNote.setOnAction(event -> {
            if (checkNote.isSelected()) {
                Platform.runLater(() -> {
                    codeArea.getScene().getRoot().lookup("#codeArea"+ finalI +" .text").setStyle("-fx-fill:grey;-fx-strikethrough: true;");
                    codeArea.setEditable(false);
                });
            } else {
                Platform.runLater(() -> {
                    codeArea.getScene().getRoot().lookup("#codeArea"+ finalI +" .text").setStyle("-fx-fill:black;-fx-strikethrough: false;");
                    codeArea.setEditable(true);
                });
            }
        });

        dragButton.setOnMousePressed(event -> {
            initialIndex = notesVbox.getChildren().indexOf(hbox);
            initialX = event.getSceneX();
            initialY = event.getSceneY();
            hboxBeingDragged = hbox;
        });
        dragButton.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - initialX;
            double offsetY = event.getSceneY() - initialY;

            hbox.setLayoutX(hbox.getLayoutX() + offsetX);
            hbox.setLayoutY(hbox.getLayoutY() + offsetY);

            initialX = event.getSceneX();
            initialY = event.getSceneY();

            int newIndex = calculateNewIndex(event.getSceneY());
            if (newIndex != initialIndex) {
                notesVbox.getChildren().remove(hbox);
                notesVbox.getChildren().add(newIndex, hbox);
                initialIndex = newIndex;
            }
        });
        dragButton.setOnMouseReleased(event -> {
            hbox.setLayoutX(0);
            hbox.setLayoutY(0);
            notesVbox.getChildren().remove(hboxBeingDragged);
            notesVbox.getChildren().add(initialIndex, hboxBeingDragged);
            hboxBeingDragged = null;
        });
        hbox.getChildren().addAll(addButton,dragButton,checkNote,codeArea);
        ((HBox)codeArea.getParent()).minHeightProperty().bind(textHeight);

// Set the minHeight of the CodeArea to the minHeight of the HBox containing it
        codeArea.minHeightProperty().bind(((HBox)codeArea.getParent()).minHeightProperty());
        setUpHBoxMouseEvents(hbox, dragButton, addButton); // Set up mouse events for this HBox
        int in = notesVbox.getChildren().indexOf(hbox)+1;
        prevLengths.add(in, 0); // Add initial length to list
        codeArea.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.BACK_SPACE) {
                int newLength = codeArea.getText().length();
                prevLengths.set(in, newLength);
            }
            if (event.getCode() == KeyCode.BACK_SPACE) {
                int oldLength = prevLengths.get(in);
                codeArea.setOnKeyReleased(releaseEvent -> {
                    int newLength = codeArea.getText().length();
                    if (prevLengths.size() > 1 && oldLength == 0 && newLength == 0) {
                        // Remove the parent HBox of the CodeArea
                        notesVbox.getChildren().remove(hbox);
                        prevLengths.remove(in);
                    } else {
                        prevLengths.set(in, newLength);
                    }
                    codeArea.setOnKeyReleased(null);
                });
            }
        });



// Allow the HBox to grow vertically as needed
        ((HBox)codeArea.getParent()).setFillHeight(true);
        if (parentHBox == null) {
            notesVbox.getChildren().add(1, hbox); // Add the new hbox to the beginning of the notesVbox
        } else {
            int index = notesVbox.getChildren().indexOf(parentHBox); // Get the index of the parent hbox
            notesVbox.getChildren().add(index + 1, hbox); // Add the new hbox just under the parent hbox
        }
        sampleHbox.setVisible(false);
        sampleHbox.setManaged(false);
        notesVbox.setPrefWidth(1290);
        notesScroll.setContent(notesVbox);
        final double SPEED = 0.01;
        notesScroll.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            notesScroll.setVvalue(notesScroll.getVvalue() - deltaY);
        });
        i++;
    }
    private void closeButtonAnimation (Pane pane) {
        FadeTransition transition;
        if (!pane.isVisible()) {
            pane.setVisible(true);
            transition = new FadeTransition(Duration.seconds(0.3), pane);
            transition.setFromValue(0.0);
            transition.setToValue(1);
            transition.setOnFinished(finishedEvent -> {
                pane.setVisible(true);
                pane.setOpacity(1);
            });
            transition.play();
        } else {
            pane.setVisible(false);
        }
    }
    private void closeButton (Button button, TabPane pane) {
        scrollBar.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED,e-> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
            Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
            if (!paneBounds.contains(mouseX, mouseY) && pane.isVisible() && !buttonBounds.contains(mouseX, mouseY)) {
                button.fire();
            }
        });
    }
    private void closeButton (Button button, Pane pane) {
        scrollBar.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED,e-> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
            Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
            if (!paneBounds.contains(mouseX, mouseY) && pane.isVisible() && !buttonBounds.contains(mouseX, mouseY)) {
                button.fire();
            }
        });
    }
    private void closeButton (Button button, Pane pane, Pane secondPane) {
        scrollBar.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED,e-> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
            Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
            if (!paneBounds.contains(mouseX, mouseY) && pane.isVisible()  && !secondPane.isVisible() && !buttonBounds.contains(mouseX, mouseY)) {
                button.fire();
            }
        });
    }
    private void closeButton (Button button, Pane pane, Pane secondPane, Pane thirdPane) {
        scrollBar.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED,e-> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
            Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
            if (!paneBounds.contains(mouseX, mouseY) && pane.isVisible() && !thirdPane.isVisible() && !secondPane.isVisible() && !buttonBounds.contains(mouseX, mouseY)) {
                button.fire();
            }
        });
    }

    private void setButtonColorAction(Button button, Label label) {
        switch (button.getText()) {
            case "Light gray":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #f2f1ef; -fx-background-radius: 3px"));
                break;
            case "Gray":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #e3e2e0; -fx-background-radius: 3px"));
                break;
            case "Brown":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #eee0da; -fx-background-radius: 3px"));
                break;
            case "Orange":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #fadec9; -fx-background-radius: 3px"));
                break;
            case "Yellow":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #fdecc8; -fx-background-radius: 3px"));
                break;
            case "Green":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #dbeddb; -fx-background-radius: 3px"));
                break;
            case "Blue":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #d3e5ef; -fx-background-radius: 3px"));
                break;
            case "Purple":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #e8deee; -fx-background-radius: 3px"));
                break;
            case "Pink":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #f5e0e9; -fx-background-radius: 3px"));
                break;
            case "Red":
                button.setOnAction(e -> label.setStyle("-fx-background-color: #ffe2dd; -fx-background-radius: 3px"));
                break;
            default:
                break;
        }
    }
    private void countAll(VBox vbox) {
        ((Button) vbox.getChildren().get(0)).setOnAction(e-> {
            resizeButtons(toDoCountButton);
            resizeButtons(doingCountButton);
            resizeButtons(doneCountButton);
            toDoCountButton.setText(String.valueOf(toDoCodeAreas.getChildren().size()));
            doingCountButton.setText(String.valueOf(doingCodeAreas.getChildren().size()));
            doneCountButton.setText(String.valueOf(doneCodeAreas.getChildren().size()));
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {
                int codeAreaCount = toDoCodeAreas.getChildren().size();
                toDoCountButton.setText(String.valueOf(codeAreaCount));
            });
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {
                int codeAreaCount = doingCodeAreas.getChildren().size();
                doingCountButton.setText(String.valueOf(codeAreaCount));
            });
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {
                int codeAreaCount = doneCodeAreas.getChildren().size();
                doneCountButton.setText(String.valueOf(codeAreaCount));
            });
        });
    }
    private void updateCount(VBox vbox, Button button) {
        int count = 0;
        for (Node node : vbox.getChildren()) {
            String name = ((CodeArea) node).getText().trim();
            if (!name.isBlank() && !name.equals("Untitled")) {
                count++;
            }
        }
        button.setText(String.valueOf(count));
    }
    private void countValues (VBox vbox) {
        ((Button) vbox.getChildren().get(1)).getContextMenu().getItems().get(0).setOnAction(e-> {
            updateCount(toDoCodeAreas, toDoCountButton);
            updateCount(doingCodeAreas, doingCountButton);
            updateCount(doneCodeAreas, doneCountButton);
            resizeButtons(toDoCountButton);
            resizeButtons(doingCountButton);
            resizeButtons(doneCountButton);
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateCount(toDoCodeAreas, toDoCountButton);});
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateCount(doingCodeAreas, doingCountButton);});
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateCount(doneCodeAreas, doneCountButton);});
        });
    }
    private void updateUniqueCount(VBox vbox, Button button) {
        int count = 0;
        Set<String> names = new HashSet<>();
        for (Node node : vbox.getChildren()) {
            if (node instanceof CodeArea) {
                String name = ((CodeArea) node).getText().trim();
                if (!name.isBlank() && !name.equals("Untitled") && !names.contains(name)) {
                    count++;
                    names.add(name);
                }
            }
        }
        button.setText(String.valueOf(count));
    }
    private void countUniqueValues (VBox vbox) {
        ((Button) vbox.getChildren().get(2)).getContextMenu().getItems().get(0).setOnAction(e-> {
            resizeButtons(toDoCountButton);
            resizeButtons(doingCountButton);
            resizeButtons(doneCountButton);
            updateUniqueCount(toDoCodeAreas, toDoCountButton);
            updateUniqueCount(doingCodeAreas, doingCountButton);
            updateUniqueCount(doneCodeAreas, doneCountButton);
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateUniqueCount(toDoCodeAreas, toDoCountButton);});
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateUniqueCount(doingCodeAreas, doingCountButton);});
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateUniqueCount(doneCodeAreas, doneCountButton);});
        });
    }
    private void updateEmpty(VBox vbox, Button button) {
        int count = 0;
        for (Node node : vbox.getChildren()) {
            String name = ((CodeArea) node).getText().trim();
            if (name.isBlank() || name.equals("Untitled")) {
                count++;
            }
        }
        button.setText(String.valueOf(count));
    }
    private void countEmpty (VBox vbox) {
        ((Button) vbox.getChildren().get(3)).getContextMenu().getItems().get(0).setOnAction(e-> {
            resizeButtons(toDoCountButton);
            resizeButtons(doingCountButton);
            resizeButtons(doneCountButton);
            updateEmpty(toDoCodeAreas, toDoCountButton);
            updateEmpty(doingCodeAreas, doingCountButton);
            updateEmpty(doneCodeAreas, doneCountButton);
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmpty(toDoCodeAreas, toDoCountButton);});
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmpty(doingCodeAreas, doingCountButton);});
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmpty(doneCodeAreas, doneCountButton);});
        });
    }
    private void updateValuesPercentage(VBox vbox, Button button) {
        int total = vbox.getChildren().size();
        int Count = 0;
        for (Node node : vbox.getChildren()) {
            String name = ((CodeArea) node).getText();
            if (!name.isBlank() && !name.equals("Untitled")) {
                Count++;
            }
        }
        double Percentage = (double) Count / total * 100;
        String formattedPercentage = new DecimalFormat("#.##").format(Percentage).replace(".00", "");
        String buttonText = formattedPercentage + "%";
        button.setText(buttonText);
        ((HBox) button.getParent()).setPrefWidth(200);
        ((HBox) button.getParent()).setMaxWidth(200);
        Text text = new Text(buttonText);
        text.setFont(button.getFont());
        double width = text.getBoundsInLocal().getWidth() + 20; // Add some padding
        button.setMaxWidth(width);
        button.setPrefWidth(width);
    }
    private void countValuesPercentage (VBox vbox) {
        ((Button) vbox.getChildren().get(4)).getContextMenu().getItems().get(0).setOnAction(e-> {
            updateValuesPercentage(toDoCodeAreas, toDoCountButton);
            updateValuesPercentage(doingCodeAreas, doingCountButton);
            updateValuesPercentage(doneCodeAreas, doneCountButton);
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateValuesPercentage(toDoCodeAreas, toDoCountButton);});
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateValuesPercentage(doingCodeAreas, doingCountButton);});
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateValuesPercentage(doneCodeAreas, doneCountButton);});
        });
    }
    private void updateEmptyPercentage(VBox vbox, Button button) {
        int total = vbox.getChildren().size();
        int emptyCount = 0;
        for (Node node : vbox.getChildren()) {
            String name = ((CodeArea) node).getText();
            if (name.isBlank() || name.equals("Untitled")) {
                emptyCount++;
            }
        }
        double emptyPercentage = (double) emptyCount / total * 100;
        String formattedPercentage = new DecimalFormat("#.##").format(emptyPercentage).replace(".00", "");
        String buttonText = formattedPercentage + "%";
        button.setText(buttonText);
        ((HBox) button.getParent()).setPrefWidth(200);
        ((HBox) button.getParent()).setMaxWidth(200);
        Text text = new Text(buttonText);
        text.setFont(button.getFont());
        double width = text.getBoundsInLocal().getWidth() + 20; // Add some padding
        button.setMaxWidth(width);
        button.setPrefWidth(width);
    }
    private void countEmptyPercentage (VBox vbox) {

        ((Button) vbox.getChildren().get(5)).getContextMenu().getItems().get(0).setOnAction(e-> {
            updateEmptyPercentage(toDoCodeAreas, toDoCountButton);
            updateEmptyPercentage(doingCodeAreas, doingCountButton);
            updateEmptyPercentage(doneCodeAreas, doneCountButton);
            toDoCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmptyPercentage(toDoCodeAreas, toDoCountButton);});
            doingCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmptyPercentage(doingCodeAreas, doingCountButton);});
            doneCodeAreas.getChildren().addListener((ListChangeListener<Node>) change -> {updateEmptyPercentage(doneCodeAreas, doneCountButton);});
        });
    }
    private void setUpCounts() {
        countAll(toDoVBox);
        countAll(doingVBox);
        countAll(doneVBox);
        countValues(toDoVBox);
        countValues(doingVBox);
        countValues(doneVBox);
        countUniqueValues(toDoVBox);
        countUniqueValues(doingVBox);
        countUniqueValues(doneVBox);
        countEmpty(toDoVBox);
        countEmpty(doingVBox);
        countEmpty(doneVBox);
        countValuesPercentage(toDoVBox);
        countValuesPercentage(doingVBox);
        countValuesPercentage(doneVBox);
        countEmptyPercentage(toDoVBox);
        countEmptyPercentage(doingVBox);
        countEmptyPercentage(doneVBox);
    }
    private void resizeButtons(Button button) {
        ((HBox) button.getParent()).setMaxWidth(106);
        ((HBox) button.getParent()).setPrefWidth(106);
        button.setMaxWidth(46);
        button.setPrefWidth(46);
    }
    /* private void setUpFilters() {
        ObservableList<CodeArea> codeAreas = FXCollections.observableArrayList();
        for (Node node : codeAreasBox.getChildren()) {
            codeAreas.add((CodeArea) node);
        }

// Create a filtered list based on the codeAreas list
        FilteredList<CodeArea> filteredCodeAreas = new FilteredList<>(codeAreas);

// Bind the filtered list to the VBox's children property
        codeAreasBox.getChildren().setAll(filteredCodeAreas);

// Set up the filter for the filtered list based on the text in the filter field
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCodeAreas.setPredicate(codeArea -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filterText = newValue.toLowerCase();
                String codeAreaText = codeArea.getText().toLowerCase();
                return codeAreaText.contains(filterText);
            });
        });
    }*/
    private void setUpTaskPage() {
        tasksCodeArea.replaceText("Untitled");
        tasksCodeArea.setStyle("-fx-font-family: System;-fx-font-size: 14");
// or perhaps setMargin would be better...
        setUpCounts();
        System.out.println(((Button) toDoVBox.getChildren().get(1)).getContextMenu().getItems().get(0).getText());
        toDoAddButton.setOnAction(e-> {
            setUpTasks(toDoCodeAreas);
            if (!toDoCountButton.getText().contains("%")) resizeButtons(toDoCountButton);
        });
        doingAddButton.setOnAction(e-> {
            setUpTasks(doingCodeAreas);
            if (!doingCountButton.getText().contains("%")) resizeButtons(doingCountButton);
        });
        doneAddButton.setOnAction(e-> {
            setUpTasks(doneCodeAreas);
            if (!doneCountButton.getText().contains("%")) resizeButtons(doneCountButton);
        });
        toDoDelete.setOnAction(e-> {deleteTasks(toDoCodeAreas);closeButtonAnimation(toDoMenu);countAll(toDoVBox);});
        doingDelete.setOnAction(e-> {deleteTasks(doingCodeAreas); closeButtonAnimation(doingMenu);countAll(doingVBox);});
        doneDelete.setOnAction(e-> {deleteTasks(doneCodeAreas);closeButtonAnimation(doneMenu);countAll(doneVBox);});
        browseTeamsButton.setOnAction(e-> toggleTeamsSettings());
        closeTeamsButton.setOnAction(e-> toggleTeamsSettings());
        closeButton(sortButton, sortPane);
        closeButton(toDoCountButton, toDoPane);
        closeButton(doingCountButton, doingPane);
        closeButton(doneCountButton, donePane);
        closeButton(toDoMenuButton, toDoMenu);
        closeButton(doingMenuButton, doingMenu);
        closeButton(doneMenuButton, doneMenu);
        closeButton(nameFilterButton, nameFilterPane, nameFilterMenuPane);
        closeButton(nameFilterOption, nameFilterMenuPane);
        closeButton(dateFilterButton, dateFilterPane, dateFilterMenuPane, dateFilterOptionsPane);
        closeButton(dateFilterOption, dateFilterMenuPane);
        closeButton(dateOptionsButton, dateFilterOptionsPane);
        closeButton(browseTeamsEllipsis, browseTeamsPane);
        closeButton(teamSettingsIconButton, teamSettingsLogoPane);

        browseTeamsEllipsis.setOnAction(e -> closeButtonAnimation(browseTeamsPane));
        toDoCountButton.setOnAction(e -> closeButtonAnimation(toDoPane));
        doingCountButton.setOnAction(e -> closeButtonAnimation(doingPane));
        doneCountButton.setOnAction(e -> closeButtonAnimation(donePane));
        toDoMenuButton.setOnAction(e -> closeButtonAnimation(toDoMenu));
        doingMenuButton.setOnAction(e -> closeButtonAnimation(doingMenu));
        doneMenuButton.setOnAction(e -> closeButtonAnimation(doneMenu));
        nameFilterButton.setOnAction(e -> closeButtonAnimation(nameFilterPane));
        nameFilterOption.setOnAction(e -> closeButtonAnimation(nameFilterMenuPane));
        dateFilterButton.setOnAction(e -> closeButtonAnimation(dateFilterPane));
        dateFilterOption.setOnAction(e -> closeButtonAnimation(dateFilterMenuPane));
        dateOptionsButton.setOnAction(e -> closeButtonAnimation(dateFilterOptionsPane));

        designButton(closeTeamsButton);
        designButton(browseTeamsEllipsis);
        designButton(browseTeamsButton);
        designButton(nameFilterButton);
        designButton(nameFilterOption);
        designButton(deleteNameFilter);
        designButton(dateFilterButton);
        designButton(dateFilterOption);
        designButton(deleteDateFilter);
        designButton(dateOptionsButton);

        for (Node node : nameFilterMenuPane.getChildren()) {for (Node button : ((VBox) node).getChildren()) {designButton((Button) button);}}
        for (Node node : dateFilterMenuPane.getChildren()) {for (Node button : ((VBox) node).getChildren()) {designButton((Button) button);}}
        for (Node node : dateFilterOptionsPane.getChildren()) {for (Node button : ((VBox) node).getChildren()) {designButton((Button) button);}}
        for (Node node : doingMenuVBox.getChildren()) {if (node instanceof Button) designButton((Button) node);}
        for (Node node : toDoMenuVBox.getChildren()) {if (node instanceof Button) designButton((Button) node);}
        for (Node node : doneVBox.getChildren()) designButton((Button) node);
        for (Node node : doingVBox.getChildren()) designButton((Button) node);
        for (Node node : toDoVBox.getChildren()) designButton((Button) node);
        designButton(filterButton);
        designButton(sortButton);
        for (Node node : sortVBox.getChildren()) designButton((Button) node);
        for (Node node : filterHBox.getChildren()) customDesignButton((Button) node);
        for (Node node : taskStatusHBox.getChildren())
            for (Node secondNode : ((HBox) node).getChildren())
                for (Node thirdNode : ((HBox) secondNode).getChildren()) designButton((Button) thirdNode);

        for (Node node : doneMenuVBox.getChildren()) {
            if (node instanceof Button) {
                designButton((Button) node);
                setButtonColorAction((Button) node, doneLabel);
            }
        }
        for (Node node : toDoMenuVBox.getChildren()) {
            if (node instanceof Button) {
                designButton((Button) node);
                setButtonColorAction((Button) node, toDoLabel);
            }
        }
        for (Node node : doingMenuVBox.getChildren()) {
            if (node instanceof Button) {
                designButton((Button) node);
                setButtonColorAction((Button) node, doingLabel);
            }
        }
        tasksCodeArea.setVisible(false);
        tasksCodeArea.setManaged(false);
        toDoCodeAreas.getChildren().remove(tasksCodeArea);
        setUpTasks(toDoCodeAreas);
        setUpTasks(doingCodeAreas);
        setUpTasks(doneCodeAreas);
    }

    private void deleteTasks(VBox parentVBox) {
        Iterator<Node> iterator = parentVBox.getChildren().iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (((CodeArea) node).getText().isBlank() || ((CodeArea) node).getText().equals("Untitled")) {
                iterator.remove();
            }
        }
    }
    private void customDesignButton (Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #eeeeee;-fx-background-radius:32;-fx-border-radius:32;-fx-border-color: #37352F26"));
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: #cccccc;-fx-background-radius:32;-fx-border-radius:32;-fx-border-color: #37352F26"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: transparent;-fx-background-radius:32;-fx-border-radius:32;-fx-border-color: #37352F26"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent;-fx-background-radius:32;-fx-border-radius:32;-fx-border-color: #37352F26"));
    }
    private void designButton(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #eeeeee"));
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: #cccccc"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: transparent"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent"));
    }
    private int calculateNewIndex(double y) {
        int newIndex = 0;
        for (int i = 0; i < notesVbox.getChildren().size(); i++) {
            Bounds bounds = notesVbox.getChildren().get(i).localToScene(notesVbox.getChildren().get(i).getBoundsInLocal());
            if (y > bounds.getMaxY()) {
                newIndex = i + 1;
            } else {
                break;
            }
        }
        return newIndex;
    }
    private void setScrollProperties(ScrollPane scrollPane) {
        scrollPane.setStyle(CSS_SCROLL_PANE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
    }
    void addEventHandlers() throws SQLException {
        contextMenu.getItems().addAll(nameItem, dateCreatedItem);
        coverScroll.setManaged(false);
        setUpTitlePage();
        for (int i = 0; i < 5; i++) {
            setUpNotes(null);
        }
        notePage.setVisible(false);
        notePage.setManaged(false);
        tasksPage.setVisible(false);
        tasksPage.setVisible(false);
        filePage.setVisible(true);
        filePage.setVisible(true);
        setUpTaskPage();
        setUpFilePage();
        addTeamNameValidator(TeamName, teamNameErrorLabel);
        TeamLogoPane.setStyle(CSS_TAB_PANE);
        teamSettingsLogoPane.setStyle(CSS_TAB_PANE);
        customSettingsTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        emojiSettingsTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        iconSettingsTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        customTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        emojiTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        iconTab.setStyle(CSS_WHITE_BACKGROUND + CSS_HAND_CURSOR + CSS_FONT_SIZE_14);
        setScrollProperties(emojiScroll);
        setScrollProperties(emojiSettingsScroll);
        setScrollProperties(iconScroll);
        setScrollProperties(iconSettingsScroll);

        Parent root = LogOut.getScene().getRoot();
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            Bounds browseTeamsListVBoxBounds = browseTeamsListVBox.localToScene(browseTeamsListVBox.getBoundsInLocal());
            Bounds teamPaneBounds = TeamPane.localToScene(TeamPane.getBoundsInLocal());
            Bounds plusButtonBounds = plusButton.localToScene(plusButton.getBoundsInLocal());
            Bounds teamSettingsPaneBounds = teamSettingsPane.localToScene(teamSettingsPane.getBoundsInLocal());
            if ((mouseX < 369 || mouseX > 773 || mouseY < 353 || mouseY > 710) &&
                    (mouseX < 556 || mouseX > 583 || mouseY < 321 || mouseY > 348) && TeamLogoPane.isVisible())
                toggleIconTile();
            else if (!teamPaneBounds.contains(mouseX, mouseY) && !plusButtonBounds.contains(mouseX, mouseY)) {
                if (HiderPane.isVisible() && !TeamLogoPane.isVisible()) {
                    toggleTeams();
                }
            }
            if (!teamSettingsPaneBounds.contains(mouseX, mouseY) && !browseTeamsListVBoxBounds.contains(mouseX, mouseY)
                    && !teamSettingsLogoPane.isVisible()) {
                if (teamHiderPane.isVisible()) {
                    try {
                        toggleTeamSettings();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, this::MouseHide);
        menuButton.setOnMouseEntered(event -> changeMenuButtonImage("menuOpen.gif"));
        menuButton.setOnMouseExited(event -> changeMenuButtonImage("menu.png"));



        teamDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            // Limit the input to 255 characters
            if (newValue.length() > MAX_TEAM_DESCRIPTION_LENGTH) {
                teamDescription.setText(oldValue);
            }
            // Update the character count label
            int totalLength = Math.min(newValue.length(), MAX_TEAM_DESCRIPTION_LENGTH);
            charactersLeft.setText("Characters left: " + (MAX_TEAM_DESCRIPTION_LENGTH - totalLength));
        });

        TeamName.textProperty().addListener((observable, oldValue, newValue) -> {
            setButtonState(newValue.trim().isEmpty(), CreateTeam);
        });

        iconLink.textProperty().addListener((observable, oldValue, newValue) -> {
            setButtonState(newValue.trim().isEmpty(), submitLinkButton);
        });
        displayTeamNames();
    }
    public void toggleTeams() {
        if (!HiderPane.isVisible()) {
            HiderPane.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), HiderPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.99);
            fadeIn.play();
        } else {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), HiderPane);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(finishedEvent -> HiderPane.setVisible(false));
            fadeOut.play();
        }
    }
    @FXML
    private void toggleTeamSettings() throws SQLException {
        if (!teamHiderPane.isVisible()) {
            teamHiderPane.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), teamHiderPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.99);
            fadeIn.play();
        } else {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), teamHiderPane);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(finishedEvent -> teamHiderPane.setVisible(false));
            fadeOut.play();
        }
    }
    public void leaveTeamCreation(ActionEvent actionEvent) {
        toggleTeams();
    }

    public void InitializeComboBox() {
        TeamType.getItems().addAll("        General team", "        Software engineering", "        Product", "        Sales", "        Marketing");
        TeamType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TeamImage.setImage(TEAM_IMAGES.get(newValue));
            } else {
                TeamImage.setImage(null);
            }
        });
    }
    @FXML private void updateTeamSettings() throws SQLException {
        String teamName = teamSettingsTeamNameField.getText().trim();
        for (Node node : teamNamesButtonBox.getChildren()) if(((JFXButton) node).getText().equals(oldTeamName)){
            ((JFXButton) node).setText(teamName);
            loadTeamButtonImage(((JFXButton) node), teamSettingsTeamIcon.getImage().getUrl());
        }
        for (Node node : browseTeamsListVBox.getChildren()) if (node instanceof JFXButton) if(((JFXButton) node).getText().equals(oldTeamName)){
            ((JFXButton) node).setText(teamName);
            loadTeamButtonImage(((JFXButton) node), teamSettingsTeamIcon.getImage().getUrl());
        }
        teamSettingsTeamName.setText(teamName);
        String teamDescriptionText = teamSettingsDescription.getText().trim();
        TeamIcon.setImage(new Image(TEAM_ICON_BASE_PATH + TEAM_ICON_DEFAULT_IMAGE));

        String iconImagePath = teamSettingsTeamIcon.getImage().getUrl();
        int id = Team.getTeamIdByName(oldTeamName);
        Team.updateTeam(teamName, iconImagePath, teamDescriptionText, id);
        toggleTeamSettings();
    }
    public void displayTeamNames() throws SQLException {
        teamNamesButtonBox.getChildren().clear();
        browseTeamsListVBox.getChildren().clear();
        teamNamesBox.getChildren().clear();
        teamNamesStackPane.setVisible(false);
        teamNamesStackPane.setManaged(false);
        int memberId = User.getMemberId(email);
        ArrayList<Team> teams = TeamMembers.getTeamsForMember(memberId);
        for (Team team : teams) {
            String teamName = team.getName();
            System.out.println(team.getName()+","+team.getDescription());
            JFXButton teamButton = createTeamButton();
            closeButton(teamButton, teamHiderPane);
            teamButton.setOnAction(e -> {
                try {
                    if (TeamMembers.getOwnershipForMember(User.getMemberId(email))==1) {
                        System.out.println("owner");
                        leaveTeamText.setStyle("-fx-text-fill: #0000007e");
                        leaveTeamTextDescription.setStyle("-fx-text-fill: #0000007e");
                        archiveTeamText.setStyle("-fx-text-fill: #eb5959");
                        archiveTeamTextDescription.setStyle("-fx-text-fill: #000000");
                        leaveTeamButton.setDisable(true);
                        archiveTeamButton.setDisable(false);
                    }
                    else {
                        System.out.println("member");
                        leaveTeamText.setStyle("-fx-text-fill: #000000");
                        leaveTeamTextDescription.setStyle("-fx-text-fill: #000000");
                        archiveTeamText.setStyle("-fx-text-fill: #eb595980");
                        archiveTeamTextDescription.setStyle("-fx-text-fill: #0000007e");
                        leaveTeamButton.setDisable(false);
                        archiveTeamButton.setDisable(true);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                teamSettingsDescription.setText(team.getDescription());
                oldTeamName = teamName;
                closeButtonAnimation(teamHiderPane);
                teamSettingsTeamName.setText(teamName);
                Image image = new Image(team.getIconFilePath());
                teamSettingsTeamIcon.setImage(image);
                teamSettingsTeamNameField.setText(teamName);
                teamSettingsUpdateButton.setStyle("-fx-background-color: #b3cdf1; -fx-text-fill: #ffffff;");
                teamSettingsUpdateButton.setDisable(true);
                teamSettingsUpdateButton.setCursor(Cursor.DEFAULT);
                iconSettingsLink.textProperty().addListener((observable, oldValue, newValue) -> {
                    setButtonState(newValue.trim().isEmpty(), submitSettingsLinkButton);
                });
                teamSettingsTeamNameField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (teamSettingsTeamNameField.getText().isBlank()) {
                        teamSettingsUpdateButton.setStyle("-fx-background-color: #b3cdf1; -fx-text-fill: #ffffff;");
                        teamSettingsUpdateButton.setDisable(true);
                        teamSettingsUpdateButton.setCursor(Cursor.DEFAULT);
                    } else {
                        teamSettingsUpdateButton.setDisable(false);
                        teamSettingsUpdateButton.setStyle("-fx-background-color: #4281db; -fx-text-fill: #ffffff;");
                        teamSettingsUpdateButton.setCursor(Cursor.HAND);
                    }
                });
                teamSettingsDescription.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (teamSettingsDescription.getText().isBlank() || teamSettingsTeamNameField.getText().isBlank()) {
                        teamSettingsUpdateButton.setStyle("-fx-background-color: #b3cdf1; -fx-text-fill: #ffffff;");
                        teamSettingsUpdateButton.setDisable(true);
                        teamSettingsUpdateButton.setCursor(Cursor.DEFAULT);
                    } else {
                        teamSettingsUpdateButton.setDisable(false);
                        teamSettingsUpdateButton.setStyle("-fx-background-color: #4281db; -fx-text-fill: #ffffff;");
                        teamSettingsUpdateButton.setCursor(Cursor.HAND);
                    }
                });
            });
            loadTeamButtonImage(teamButton, team.getIconFilePath());
            teamButton.setText(teamName);
            teamButton.setFont(teamNameButton.getFont());
            teamButton.setPrefHeight(teamNameButton.getPrefHeight());
            teamButton.setPrefWidth(teamNameButton.getPrefWidth());
            browseTeamsListVBox.getChildren().add(teamButton);
            browseTeamsListVBox.requestLayout();
        }
        for (Team team : teams) {
            String teamName = team.getName();
            JFXButton teamButton = createTeamButton();
            Label teamLabel = createTeamLabel("");
            loadTeamButtonImage(teamButton, team.getIconFilePath());
            teamButton.setText(teamName);
            teamButton.setFont(teamNameButton.getFont());
            teamButton.setPrefHeight(teamNameButton.getPrefHeight());
            teamButton.setPrefWidth(teamNameButton.getPrefWidth());
            teamNamesButtonBox.getChildren().add(teamButton);
            teamNamesBox.getChildren().add(teamLabel);
        }
    }

    public void TeamCreate() throws SQLException {
        String teamName = TeamName.getText().trim();
        String teamDescriptionText = teamDescription.getText().trim();
        if (!isValidTeamName(teamName)) {
            return;
        }
        toggleTeams();
        JFXButton teamButton = createTeamButton();
        teamButton.setText(teamName);
        Label teamLabel = createTeamLabel("");
        teamNamesButtonBox.getChildren().add(teamButton);
        teamNamesBox.getChildren().add(teamLabel);
        setTeamButtonImage(teamButton, teamName);
        Team.createTeam(teamName, iconImagePath, teamDescriptionText);
        int id=User.getMemberId(email);
        Team.addMember(teamName,id, 1);
        TeamName.clear();
        teamDescription.clear();
        displayTeamNames();
    }

    private Label createTeamLabel(String teamName) {
        Label teamLabel = new Label(teamName);
        teamLabel.setStyle("-fx-text-fill: rgba(25,23,17,0.6); -fx-font-family: 'Calibri Bold'; -fx-font-size: 13px; -fx-padding: 2 10 2 35; -fx-min-height: 0; -fx-pref-height: 27; -fx-pref-width: 240;");
        return teamLabel;
    }
    private JFXButton createTeamButton() {
        JFXButton teamButton = new JFXButton("");
        teamButton.setAlignment(Pos.TOP_LEFT);
        teamButton.setOnAction(e -> {
            // handle button click
            // e.g. navigate to team view
        });
        teamButton.setStyle("-fx-background-color: #f7f7f5; -fx-text-fill: rgba(25,23,17,0.6); -fx-font-family: 'Calibri Bold'; -fx-font-size: 13px; -fx-padding: 2 10 2 10; -fx-min-height: 0; -fx-pref-height: 27; -fx-pref-width: 240;");
        teamButton.setCursor(Cursor.HAND);
        return teamButton;
    }
    private String iconImagePath;
    private void loadTeamButtonImage(JFXButton teamButton, String icon_file_path) {
        Image image = new Image(icon_file_path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        teamButton.setGraphic(imageView);
    }

        private void setTeamButtonImage(JFXButton teamButton, String teamName) {
        iconImagePath = TeamIcon.getImage().getUrl();
        Image image;
        System.out.println(iconImagePath);
        if (iconImagePath.equals(TEAM_ICON_BASE_PATH + TEAM_ICON_DEFAULT_IMAGE)) {
            image = new Image(TEAM_ICON_BASE_PATH + teamName.toUpperCase().charAt(0) + ".png");
            iconImagePath = TEAM_ICON_BASE_PATH + teamName.toUpperCase().charAt(0) + ".png";
        } else {
            image = new Image(iconImagePath);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        teamButton.setGraphic(imageView);
    }
}




