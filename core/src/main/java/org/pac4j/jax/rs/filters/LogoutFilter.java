package org.pac4j.jax.rs.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Providers;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.DefaultLogoutLogic;
import org.pac4j.core.engine.LogoutLogic;
import org.pac4j.jax.rs.pac4j.JaxRsContext;
import org.pac4j.jax.rs.pac4j.JaxRsProfileManager;

/**
 *
 * @author Victor Noel - Linagora
 * @since 1.0.0
 *
 */
@Priority(Priorities.AUTHORIZATION)
public class LogoutFilter extends AbstractFilter {

    private LogoutLogic logoutLogic;

    private String defaultUrl;

    private String logoutUrlPattern;

    private Boolean localLogout;

    private Boolean destroySession;

    private Boolean centralLogout;

    public LogoutFilter(Providers providers) {
        super(providers);
    }

    @Override
    protected void filter(JaxRsContext context) throws IOException {
        Config config = getConfig();
        SessionStore sessionStore = getSessionStore();

        buildLogic(config).perform(context, sessionStore, config, adapter(config),
                context.getAbsolutePath(defaultUrl, false), context.getAbsolutePath(logoutUrlPattern, false),
                localLogout, destroySession, centralLogout);
    }

    protected LogoutLogic buildLogic(Config config) {
        if (logoutLogic != null) {
            return logoutLogic;
        } else if (config.getLogoutLogic() != null) {
            return config.getLogoutLogic();
        } else {
            DefaultLogoutLogic logic = new DefaultLogoutLogic();
            logic.setProfileManagerFactory((ctx, sessionStore) -> new JaxRsProfileManager(ctx, sessionStore));
            return logic;
        }

    }

    public String getDefaultUrl() {
        return this.defaultUrl;
    }

    public void setDefaultUrl(final String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getLogoutUrlPattern() {
        return logoutUrlPattern;
    }

    public void setLogoutUrlPattern(String logoutUrlPattern) {
        this.logoutUrlPattern = logoutUrlPattern;
    }

    public Boolean getLocalLogout() {
        return localLogout;
    }

    public void setLocalLogout(Boolean localLogout) {
        this.localLogout = localLogout;
    }

    public Boolean getDestroySession() {
        return destroySession;
    }

    public void setDestroySession(Boolean destroySession) {
        this.destroySession = destroySession;
    }

    public Boolean getCentralLogout() {
        return centralLogout;
    }

    public void setCentralLogout(Boolean centralLogout) {
        this.centralLogout = centralLogout;
    }

    public LogoutLogic getLogoutLogic() {
        return logoutLogic;
    }

    public void setLogoutLogic(LogoutLogic applicationLogoutLogic) {
        this.logoutLogic = applicationLogoutLogic;
    }
}
