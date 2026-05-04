package edu.uph.m23si1.microgreens.integration;

import static org.junit.Assert.assertEquals;

import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import edu.uph.m23si1.microgreens.LoginActivity;
import edu.uph.m23si1.microgreens.MicrogreensApp;
import edu.uph.m23si1.microgreens.R;

/**
 * Integration-style test: Activity + layout + input validation path (auth UI, no network call when empty).
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34, application = MicrogreensApp.class)
public class LoginActivityIntegrationTest {

    @Test
    public void login_emptyFields_showsValidationToast() {
        ApplicationProvider.getApplicationContext()
                .getSharedPreferences("user_pref", android.content.Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();

        ActivityController<LoginActivity> controller =
                Robolectric.buildActivity(LoginActivity.class).setup();
        LoginActivity activity = controller.get();
        Button login = activity.findViewById(R.id.loginBtn);
        login.performClick();
        controller.destroy();

        assertEquals("Isi semua field", ShadowToast.getTextOfLatestToast());
    }
}
