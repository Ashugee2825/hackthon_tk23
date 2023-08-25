package guest.profile;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class guestProfileCntrl
 */

//localhost:8080/GuestDetail/guest/guestProfileCntrl?page=guestProfileDashboard&opr=showAll&pageNo=1&limit=100

@WebServlet("/guest/guestProfileCntrl")
public class GuestProfileCntrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GuestProfileCntrl() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		String page= request.getParameter("page");
		String opr = request.getParameter("opr");
		int pageNo = (null==request.getParameter("pageNo")?0:Integer.parseInt(request.getParameter("pageNo")));
		int limit= (null==request.getParameter("pageNo")?0:Integer.parseInt(request.getParameter("limit")));
		
		RequestDispatcher rd;
		GuestProfileDBService guestProfileDBService =new GuestProfileDBService();
		GuestProfile guestProfile =new GuestProfile();
		//Action for close buttons
		String homeURL=(null==request.getSession().getAttribute("homeURL")?"":(String)request.getSession().getAttribute("homeURL"));		
		if(page.equals("guestProfileDashboard"))
		{
			request.getSession().setAttribute("homePage",page);
			homeURL="guestProfileCntrl?page="+page+"&opr=showAll&pageNo="+pageNo+"&limit="+limit;
			request.getSession().setAttribute("homeURL",homeURL);
			
			if(opr.equals("showAll")) 
			{
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				
				if(pageNo==0)
					guestProfileList = guestProfileDBService.getguestProfileList();
				else { //pagination
					int totalPages= guestProfileDBService.getTotalPages(limit);
					guestProfileList = guestProfileDBService.getguestProfileList(pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("guestProfileDashboard.jsp");
				rd.forward(request, response);
			} 
			else if(opr.equals("addNew")) //CREATE
			{
				guestProfile.setDefaultValues();
				guestProfile.displayValues();
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("addNewGuestProfile.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("edit")) //UPDATE
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("updateguestProfile.jsp");
				rd.forward(request, response);
			}
			//Begin: modified by Dr PNH on 06-12-2022
			else if(opr.equals("editNext")) //Save and Next
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("updateNextguestProfile.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("saveShowNext")) //Save, show & next
			{
				guestProfile.setDefaultValues();
				guestProfile.displayValues();
				request.setAttribute("guestProfile",guestProfile);
				
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList();
				else { //pagination
					int totalPages= guestProfileDBService.getTotalPages(limit);
					guestProfileList = guestProfileDBService.getguestProfileList(pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("saveShowNextguestProfile.jsp");
				rd.forward(request, response);
			}
			//End: modified by Dr PNH on 06-12-2022
			else if(opr.equals("delete")) //DELETE
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile.setId(id);
				guestProfileDBService.deleteguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("deleteguestProfileSuccess.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("view")) //READ
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("viewguestProfile.jsp");
				rd.forward(request, response);
			}
			
		}
		else if(page.equals("addNewguestProfile")) 
		{
			if(opr.equals("save"))
			{
				guestProfile.setRequestParam(request);
				guestProfile.displayValues();
				guestProfileDBService.createguestProfile(guestProfile);
				request.setAttribute("guestProfile",guestProfile);
				if(pageNo!=0) {//pagination
					int totalPages= guestProfileDBService.getTotalPages(limit);
					homeURL="guestProfileCntrl?page=guestProfileDashboard&opr=showAll&pageNo="+totalPages+"&limit="+limit;
					request.getSession().setAttribute("homeURL", homeURL);
				}
				rd = request.getRequestDispatcher("addNewguestProfileSuccess.jsp");
				rd.forward(request, response);
			}
		}
		//Begin: modified by Dr PNH on 06-12-2022
		else if(page.equals("updateNextguestProfile")) 
		{
			if(opr.equals("update"))
			{
				guestProfile.setRequestParam(request);
				guestProfileDBService.updateguestProfile(guestProfile);
				request.setAttribute("guestProfile",guestProfile);
				request.getSession().setAttribute("msg", "Record saved successfully");
				response.sendRedirect("guestProfileCntrl?page=guestProfileDashboard&opr=editNext&pageNo=0&limit=0&id=10");			}
		}
		else if(page.equals("saveShowNextguestProfile")) 
		{
			request.getSession().setAttribute("homePage",page);
			homeURL="guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0";
			request.getSession().setAttribute("homeURL",homeURL);
			if(opr.equals("addNew")) //save new record
			{
				guestProfile.setRequestParam(request);
				guestProfile.displayValues();
				guestProfileDBService.createguestProfile(guestProfile);
				request.setAttribute("guestProfile",guestProfile);
				if(pageNo!=0) {//pagination
					int totalPages= guestProfileDBService.getTotalPages(limit);
					homeURL="guestProfileCntrl?page=guestProfileDashboard&opr=showAll&pageNo="+totalPages+"&limit="+limit;
					request.getSession().setAttribute("homeURL", homeURL);
				}
				request.getSession().setAttribute("msg", "Record saved successfully");
				response.sendRedirect("guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0");
				//rd = request.getRequestDispatcher("guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0");
				//rd.forward(request, response);
			}
			else if(opr.equals("edit"))
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList();
				else { //pagination
					int totalPages= guestProfileDBService.getTotalPages(limit);
					guestProfileList = guestProfileDBService.getguestProfileList(pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("saveShowNextguestProfile.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("update"))
			{
				guestProfile.setRequestParam(request);
				guestProfileDBService.updateguestProfile(guestProfile);
				request.setAttribute("guestProfile",guestProfile);
				request.getSession().setAttribute("msg", "Record updated successfully");
				response.sendRedirect("guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0");
			}
			else if(opr.equals("delete"))
			{
					int id = Integer.parseInt(request.getParameter("id"));
					guestProfile.setId(id);
					guestProfileDBService.deleteguestProfile(id);
					request.setAttribute("guestProfile",guestProfile);
					request.getSession().setAttribute("msg", "Record deleted successfully");
					response.sendRedirect("guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0");		
			}
			else if(opr.equals("reset")||opr.equals("cancel"))
			{
					response.sendRedirect("guestProfileCntrl?page=guestProfileDashboard&opr=saveShowNext&id=10&pageNo=0&limit=0");		
			}
			
		}
		//End: modified by Dr PNH on 06-12-2022
		else if(page.equals("updateguestProfile")) 
		{
			if(opr.equals("update"))
			{
				guestProfile.setRequestParam(request);
				guestProfileDBService.updateguestProfile(guestProfile);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("updateguestProfileSuccess.jsp");
				rd.forward(request, response);
			}
		}
		else if(page.equals("viewguestProfile")) 
		{
			if(opr.equals("print")) 
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("printguestProfile.jsp");
				rd.forward(request, response);
			}
		}
		else if(page.equals("searchguestProfile"))
		{
			request.getSession().setAttribute("homePage",page);
			homeURL="guestProfileCntrl?page="+page+"&opr=showAll&pageNo="+pageNo+"&limit="+limit;
			request.getSession().setAttribute("homeURL",homeURL);
			if(opr.equals("search")) 
			{
				guestProfile.setRequestParam(request);
				guestProfile.displayValues();
				request.getSession().setAttribute("guestProfileSearch",guestProfile);
				request.setAttribute("opr","search");
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList(guestProfile);
				else { //pagination
					int totalPages=0;
					if(limit==0)
					totalPages=0;
					else
					totalPages=guestProfileDBService.getTotalPages(guestProfile,limit);
					pageNo=1;
					guestProfileList = guestProfileDBService.getguestProfileList(guestProfile,pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("searchguestProfile.jsp?pageNo="+pageNo+"&limit="+limit);
				rd.forward(request, response);
			}
//begin:code for download/print button
			else if(opr.equals("downloadPrint")) 
			{
				guestProfile.setRequestParam(request);
				guestProfile.displayValues();
				request.getSession().setAttribute("guestProfileSearch",guestProfile);
				request.setAttribute("opr","guestProfile");
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList(guestProfile);
				else { //pagination
					int totalPages=0;
					if(limit==0)
					totalPages=0;
					else
					totalPages=guestProfileDBService.getTotalPages(guestProfile,limit);
					pageNo=1;
					guestProfileList = guestProfileDBService.getguestProfileList(guestProfile,pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("searchguestProfileDownloadPrint.jsp?pageNo="+pageNo+"&limit="+limit);
				rd.forward(request, response);
			}
			//end:code for download/print button
			
			else if(opr.equals("showAll")) 
			{
				guestProfile=(GuestProfile)request.getSession().getAttribute("guestProfileSearch");
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList(guestProfile);
				else { //pagination
					int totalPages= guestProfileDBService.getTotalPages(guestProfile,limit);
					guestProfileList = guestProfileDBService.getguestProfileList(guestProfile,pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("searchguestProfile.jsp?pageNo="+pageNo+"&limit="+limit);
				rd.forward(request, response);
			}
			else if(opr.equals("searchNext")||opr.equals("searchPrev")||opr.equals("searchFirst")||opr.equals("searchLast")) 
			{
				request.setAttribute("opr","search");
				guestProfile=(GuestProfile)request.getSession().getAttribute("guestProfileSearch");
				ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
				if(pageNo==0)
				guestProfileList = guestProfileDBService.getguestProfileList(guestProfile);
				else { //pagination
					int totalPages= guestProfileDBService.getTotalPages(guestProfile,limit);
					guestProfileList = guestProfileDBService.getguestProfileList(guestProfile,pageNo,limit);
					request.setAttribute("totalPages",totalPages);
				}
				request.setAttribute("guestProfileList",guestProfileList);
				rd = request.getRequestDispatcher("searchguestProfile.jsp?pageNo="+pageNo+"&limit="+limit);
				rd.forward(request, response);
			}
			else if(opr.equals("showNone"))
			{
				guestProfile.setDefaultValues();
				guestProfile.displayValues();
				request.getSession().setAttribute("guestProfileSearch",guestProfile);
				request.setAttribute("opr","showNone");
				rd = request.getRequestDispatcher("searchguestProfile.jsp?pageNo="+pageNo+"&limit="+limit);
				rd.forward(request, response);
			}
			else if(opr.equals("edit")) 
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("updateguestProfile.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("delete")) 
			{
				int id = Integer.parseInt(request.getParameter("id"));
				guestProfile.setId(id);
				guestProfileDBService.deleteguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("deleteguestProfileSuccess.jsp");
				rd.forward(request, response);
			}
			else if(opr.equals("view")) 
			{
 			int id = Integer.parseInt(request.getParameter("id"));
				guestProfile = guestProfileDBService.getguestProfile(id);
				request.setAttribute("guestProfile",guestProfile);
				rd = request.getRequestDispatcher("viewguestProfile.jsp");
				rd.forward(request, response);
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static void main(String[] args) throws URISyntaxException {
		URI uri = new URI("page=updateguestProfile&opr=close&homePage=guestProfileDashboard");
		String v = uri.getQuery();
		
	}
}
