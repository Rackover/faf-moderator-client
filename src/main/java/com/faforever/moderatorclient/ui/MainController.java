package com.faforever.moderatorclient.ui;

import com.faforever.moderatorclient.api.event.FafApiFailGetEvent;
import com.faforever.moderatorclient.ui.main_window.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
@Slf4j
public class MainController implements Controller<TabPane> {
    public Tab userManagementTab;
    public Tab ladderMapPoolTab;
    public Tab mapVaultTab;
    public Tab modVaultTab;
    public Tab avatarsTab;
    public Tab recentActivityTab;
    public Tab domainBlacklistTab;
    public Tab banTab;
    public Tab votingTab;

    private final UiService uiService;

    public TabPane root;
    private UserManagementController userManagementController;
    private LadderMapPoolController ladderMapPoolController;
    private MapVaultController mapVaultController;
    private AvatarsController avatarsController;
    private RecentActivityController recentActivityController;
    private DomainBlacklistController domainBlacklistController;
    private BansController bansController;
    private VotingController votingController;

    public MainController(UiService uiService) {
        this.uiService = uiService;
    }

    @Override
    public TabPane getRoot() {
        return root;
    }

    public void initializeAfterLogin() {
        initUserManagementTab();
        initLadderMapPoolTab();
        initMapVaultTab();
        initAvatarTab();
        initRecentActivityTab();
        initDomainBlacklistTab();
        initBanTab();
        initVotingTab();
    }


    private void initUserManagementTab() {
        userManagementController = uiService.loadFxml("ui/main_window/userManagement.fxml");
        userManagementTab.setContent(userManagementController.getRoot());
    }

    private void initLadderMapPoolTab() {
        ladderMapPoolController = uiService.loadFxml("ui/main_window/ladderMapPool.fxml");
        ladderMapPoolTab.setContent(ladderMapPoolController.getRoot());
    }

    private void initMapVaultTab() {
        mapVaultController = uiService.loadFxml("ui/main_window/mapVault.fxml");
        mapVaultTab.setContent(mapVaultController.getRoot());
    }

    private void initAvatarTab() {
        avatarsController = uiService.loadFxml("ui/main_window/avatars.fxml");
        avatarsTab.setContent(avatarsController.getRoot());
    }

    private void initRecentActivityTab() {
        recentActivityController = uiService.loadFxml("ui/main_window/recentActivity.fxml");
        recentActivityTab.setContent(recentActivityController.getRoot());
    }

    private void initDomainBlacklistTab() {
        domainBlacklistController = uiService.loadFxml("ui/main_window/domainBlacklist.fxml");
        domainBlacklistTab.setContent(domainBlacklistController.getRoot());
    }

    private void initBanTab() {
        bansController = uiService.loadFxml("ui/main_window/bans.fxml");
        banTab.setContent(bansController.getRoot());
    }

    private void initVotingTab() {
        votingController = uiService.loadFxml("ui/main_window/voting.fxml");
        votingTab.setContent(votingController.getRoot());
    }

    public void display() {
        LoginController loginController = uiService.loadFxml("ui/login.fxml");

        Stage loginDialog = new Stage();
        loginDialog.setOnCloseRequest(event -> System.exit(0));
        loginDialog.setAlwaysOnTop(true);
        loginDialog.setTitle("FAF Moderator Client");
        Scene scene = new Scene(loginController.getRoot());
        scene.getStylesheets().add(getClass().getResource("/style/main.css").toExternalForm());
        loginDialog.setScene(scene);
        loginDialog.initStyle(StageStyle.UTILITY);
        loginDialog.showAndWait();

        initializeAfterLogin();

        ladderMapPoolController.refresh();
        refreshAvatars();
        refreshRecentActivity();
        domainBlacklistController.refresh();
        bansController.onRefreshBans();
        votingController.onRefreshSubjects();
    }


    public void refreshAvatars() {
        avatarsController.refresh();
    }

    @EventListener
    public void onFafApiGetFailed(FafApiFailGetEvent event) {
        Platform.runLater(() ->
                ViewHelper.exceptionDialog("Querying data from API failed", MessageFormat.format("Something went wrong while fetching data of type ''{0}'' from the API. The related controls are shown empty instead now. You can proceed without causing any harm, but it is likely that some operations will not work and/or the error will pop up again.\n\nPlease contact the maintainer and give him the details from the box below.", event.getEntityClass().getSimpleName()), event.getCause(), Optional.of(event.getUrl())));
    }

    public void refreshRecentActivity() {
        recentActivityController.refresh();
    }
}