package edu.uph.m23si1.microgreens.integration;

import static org.junit.Assert.assertEquals;

import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import edu.uph.m23si1.microgreens.MicrogreensApp;
import edu.uph.m23si1.microgreens.R;
import edu.uph.m23si1.microgreens.RegisterActivity;

/**
 * Integration-style test: registration screen validation before Firebase is called.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34, application = MicrogreensApp.class)
public class RegisterActivityIntegrationTest {

    @Test
    public void register_emptyFields_showsValidationToast() {
        ActivityController<RegisterActivity> controller =
                Robolectric.buildActivity(RegisterActivity.class).setup();
        RegisterActivity activity = controller.get();
        Button register = activity.findViewById(R.id.registerBtn);
        register.performClick();
        controller.destroy();

        assertEquals("Isi semua field", ShadowToast.getTextOfLatestToast());
    }
}
