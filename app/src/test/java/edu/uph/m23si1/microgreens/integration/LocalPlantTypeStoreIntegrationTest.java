package edu.uph.m23si1.microgreens.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.List;

import edu.uph.m23si1.microgreens.Model.PlantType;
import edu.uph.m23si1.microgreens.data.LocalPlantTypeStore;

/**
 * Integration tests: SharedPreferences + JSON persistence for user plant types (local storage).
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class LocalPlantTypeStoreIntegrationTest {

    private static final String PREF = "local_plant_types";

    private Context ctx;

    @Before
    public void setUp() {
        ctx = ApplicationProvider.getApplicationContext();
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().commit();
    }

    @Test
    public void saveUserType_thenLoadUserTypes_containsEntry() {
        LocalPlantTypeStore.saveUserType(ctx, "Basil Hijau", "/tmp/basil.png");
        List<PlantType> types = LocalPlantTypeStore.loadUserTypes(ctx);
        assertEquals(1, types.size());
        assertEquals("basil_hijau", types.get(0).getId());
        assertEquals("Basil Hijau", types.get(0).getDisplayName());
        assertEquals("/tmp/basil.png", types.get(0).getLocalImagePath());
    }

    @Test
    public void saveUserType_sameSlug_replacesInsteadOfDuplicating() {
        LocalPlantTypeStore.saveUserType(ctx, "Arugula", null);
        LocalPlantTypeStore.saveUserType(ctx, "Arugula", "/img/x.jpg");
        List<PlantType> types = LocalPlantTypeStore.loadUserTypes(ctx);
        assertEquals(1, types.size());
        assertEquals("/img/x.jpg", types.get(0).getLocalImagePath());
    }

    @Test
    public void deleteUserTypeById_removesFromPreferences() {
        LocalPlantTypeStore.saveUserType(ctx, "Kale", null);
        LocalPlantTypeStore.deleteUserTypeById(ctx, "kale");
        assertTrue(LocalPlantTypeStore.loadUserTypes(ctx).isEmpty());
    }

    @Test
    public void imagesDir_returnsWritableDirectory() {
        File dir = LocalPlantTypeStore.imagesDir(ctx);
        assertNotNull(dir);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }

    @Test
    public void loadUserTypes_corruptJson_returnsEmptyList() {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString("types_json", "not-json")
                .commit();
        List<PlantType> types = LocalPlantTypeStore.loadUserTypes(ctx);
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }
}
