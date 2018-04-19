package life.qbic;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedPortletSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import life.qbic.portal.liferayandvaadinhelpers.main.LiferayAndVaadinUtils;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class {{ cookiecutter.main_ui }} extends UI {

    private static Log log = LogFactoryUtil.getLog({{ cookiecutter.main_ui }}.class);

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        String userID = "MISSING SCREENNAME";
        Label label;
        if (LiferayAndVaadinUtils.isLiferayPortlet()) {
            String portletContextName = getPortletContextName(request);
            Integer numOfRegisteredUsers = getPortalCountOfRegisteredUsers();
            userID = LiferayAndVaadinUtils.getUser().getScreenName();
            label = new Label(
                    "Hello, " + userID + "!<br>This is portlet "
                            + portletContextName
                            + ".<br>This portal has "
                            + numOfRegisteredUsers
                            + " registered users (according to the data returned by Liferay API call).",
                    ContentMode.HTML);
        } else {
            final StringBuilder builder = new StringBuilder("You are currently in a local testing mode. No Liferay Portlet context found.<br>");
            builder.append("These are the variables you provided to generate this portlet:<br><br>");
            // generated by cookiecutter
            // FIXME:{% for key, value in cookiecutter.iteritems() %}
            // FIXME:    builder.append("\"{{ key }}\": \"{{ value }}\"<br>");
            // FIXME: {% endfor %}
            builder.append("Variables are not available at the moment, sorry. You can continue, though. See https://github.com/qbicsoftware/template_qbicportlet/issues/32");
            // --
            label = new Label(builder.toString(), ContentMode.HTML);
        }

        final Button button = new Button("Click Me");
        button.addClickListener((Button.ClickListener) event -> layout.addComponent(label));
        layout.addComponent(button);
    }

    private String getPortletContextName(VaadinRequest request) {
        WrappedPortletSession wrappedPortletSession = (WrappedPortletSession) request
                .getWrappedSession();
        PortletSession portletSession = wrappedPortletSession
                .getPortletSession();

        final PortletContext context = portletSession.getPortletContext();
        final String portletContextName = context.getPortletContextName();
        return portletContextName;
    }

    private Integer getPortalCountOfRegisteredUsers() {
        Integer result = null;

        try {
            result = UserLocalServiceUtil.getUsersCount();
        } catch (SystemException e) {
            log.error(e);
        }

        return result;
    }
}
