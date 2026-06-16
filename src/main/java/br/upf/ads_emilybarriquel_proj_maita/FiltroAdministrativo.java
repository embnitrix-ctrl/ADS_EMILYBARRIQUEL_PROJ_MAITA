package br.upf.ads_emilybarriquel_proj_maita;

import br.upf.ads_emilybarriquel_proj_maita.entity.FuncionarioEntity;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

    @WebFilter(urlPatterns = {"/faces/admin/*", "/admin/*"})
    public class FiltroAdministrativo implements Filter {

    private FilterConfig filterConfig = null;

    public FiltroAdministrativo() {
    }

  @Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    System.out.println(">>> FILTRO CHAMADO: " + httpRequest.getRequestURI());

    httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    httpResponse.setHeader("Pragma", "no-cache");
    httpResponse.setDateHeader("Expires", 0);

    HttpSession session = httpRequest.getSession(false);

    System.out.println(">>> SESSAO: " + session);
    System.out.println(">>> FUNCIONARIO: " + (session != null ? session.getAttribute("funcionarioLogado") : "null"));

    FuncionarioEntity funcionarioLogado = (session != null)
            ? (FuncionarioEntity) session.getAttribute("funcionarioLogado")
            : null;

    if (funcionarioLogado == null) {
        System.out.println(">>> REDIRECIONANDO PARA LOGIN");
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/login.xhtml");
    } else {
        System.out.println(">>> ACESSO PERMITIDO");
        chain.doFilter(request, response);
    }
}}