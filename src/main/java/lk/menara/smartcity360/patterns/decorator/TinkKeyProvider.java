package lk.menara.smartcity360.patterns.decorator;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.RegistryConfiguration;
import com.google.crypto.tink.aead.AeadConfig;
import java.security.GeneralSecurityException;
import lk.menara.smartcity360.exception.StreamProcessingException;


public final class TinkKeyProvider {

    static {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private TinkKeyProvider() {
    }

    public static Aead newAes256GcmAead() {
        try {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES256_GCM"));
            return keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead.class);
        } catch (GeneralSecurityException e) {
            throw new StreamProcessingException("Could not create Tink AEAD", e);
        }
    }
}