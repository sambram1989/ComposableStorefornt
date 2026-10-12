/**
 *
 */
package com.wholescripts.occ.filter;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import de.hybris.platform.commerceservices.user.UserMatchingService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;



public class WholescriptsUserMatchingFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsUserMatchingFilter.class);
	private static final String ACTING_USER_ROLE = "ACTING_USER_ROLE";
	private SessionService sessionService;
	@Resource(name = "userMatchingService")
	private UserMatchingService userMatchingService;
	@Resource(name = "userService")
	private UserService userService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
									final FilterChain filterChain) throws ServletException, IOException
	{
		final Authentication auth = getAuth();
		if(Objects.nonNull(auth) && Objects.nonNull(auth.getAuthorities())) {
			getSessionService().setAttribute(ACTING_USER_ROLE, auth.getAuthorities());
			final List userRole = new ArrayList<>(auth.getAuthorities());
			Optional<String> isAsagent=userRole.stream().filter(r -> WholescriptscoreConstants.ASAGENT_GROUP.equalsIgnoreCase(r.toString())).findAny();
			if (isAsagent.isPresent()) {
				String userId= request.getHeader("emulated-session-user");
				if(StringUtils.isNotEmpty(userId)) {
					setCurrentUser(userId);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	protected Authentication getAuth()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected void setCurrentUser(final String id)
	{
		try
		{
			final UserModel user = userMatchingService.getUserByProperty(id, UserModel.class);
			setCurrentUser(user);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.debug(ex.getMessage(), ex);
			throw ex;
		}
	}


	protected void setCurrentUser(final UserModel user)
	{
		userService.setCurrentUser(user);
	}


}
