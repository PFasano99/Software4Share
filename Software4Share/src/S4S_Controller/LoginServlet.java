package S4S_Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import S4S_Database.UserDAO;
import S4S_Model.User;
@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet
{
	/*
	 * per il bottone ricordami inserire il cookie e gestirlo tramite id dell ' utente prendi da sessione e database 
	 * se dc'e nel database inserisci l'id nel cookjie e  se ce il cookuie allora prenbdi l'id e inseriscilo nell
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String username=req.getParameter("username");/*Prendo l'useraname dalla richiesta*/
		String redirectPage="";/*Serve ad indicare in quale pagina devo essere reindirizzato*/
		
		HttpSession session=req.getSession();

		synchronized (session)
		{
		
			try {
					if(UserDAO.isUser(username))/*La funzione verifica se c'� l'utente*/
					{
						User user=UserDAO.FindUser(username);/*Se l'utente c'� allora lo trova e mi salva i dati nell'ogetto user*/
						redirectPage+="homepage.jsp";/*Visto che l'utente � stato trovato posso andare nella pagina corretta */
						session.setAttribute("authenticated", true);/*Questo attributo servir� nelle jsp per verificare se l'utente � autenticato*/
						session.setAttribute("user", user);/*Questo serve per iserire i dati dell'utente nella sessione*/
					
					}
				else if(UserDAO.isAdmin(username))
				{
					User user=UserDAO.FindAdministrator(username);/*Se non c'� l'utente verifica se � un amministratore e se c'� lo salva in un oggetto user */
					
					redirectPage+="homepage.jsp";/*Visto che l'utente � stato trovato posso andare nella pagina corretta */
					session.setAttribute("authenticated", true);/*Questo attributo servir� nelle jsp per verificare se l'utente � autenticato*/
					session.setAttribute("user", user);/*Questo serve per iserire i dati dell'utente nella sessione attraverso l'oggetto user posso capire se l'utente � un amministratore */
					
				
				}
				else
				{
					session.setAttribute("authenticated", false);/*Attenzione � utile serve ad indicare che l'utente non � autenticato*/
					redirectPage="login.jsp";//ritorno alla pagina di login
		
				}
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			}
			finally {
				resp.sendRedirect(redirectPage);
			}
		}
		
		
	}
}
