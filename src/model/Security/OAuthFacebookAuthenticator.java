package model.Security;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.scope.ScopeBuilder;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import static com.restfb.logging.RestFBLogger.CLIENT_LOGGER;


public class OAuthFacebookAuthenticator {

    private static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";

    private String appId = "2864933576924620";
    private String appSecret = "5b11cb850b5100f411224c64f188360f";

    private Stage stage;


    public void startLogin() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        System.out.println("In startLogin");


        // obtain Facebook access token by loading the login page
        stage = new Stage();
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
        String loginDialogUrl = facebookClient.getLoginDialogUrl(appId, SUCCESS_URL, new ScopeBuilder());
        System.out.println(loginDialogUrl);
        webEngine.load(loginDialogUrl + "&display=popup&response_type=code");
        webEngine.locationProperty().addListener((property, oldValue, newValue) -> {
                    if (newValue.startsWith(SUCCESS_URL)) {
                        // extract access token
                        CLIENT_LOGGER.debug(newValue);
                        int codeOffset = newValue.indexOf("code=");
                        String code = newValue.substring(codeOffset + "code=".length());
                        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(
                                appId, appSecret, SUCCESS_URL, code);

                        // trigger further code's execution
                        consumeAccessToken(accessToken);

                        // close the app as goal was reached
                        stage.close();
                        try {
                            //controller.goToScreen2(accessToken.getAccessToken());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if ("https://www.facebook.com/dialog/close".equals(newValue)) {
                        throw new IllegalStateException("dialog closed");
                    }
                }
        );

        Scene scene = new Scene(webView);
        stage.setScene(scene);
        stage.show();
    }

    private static void consumeAccessToken(FacebookClient.AccessToken accessToken) {
        CLIENT_LOGGER.debug("Access token: " + accessToken.getAccessToken());
        CLIENT_LOGGER.debug("Expires: " + accessToken.getExpires());
    }
}
