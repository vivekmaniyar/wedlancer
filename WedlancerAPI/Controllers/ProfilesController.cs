using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WedlancerAPI.Authorize;
using WedlancerAPI.Models;

namespace WedlancerAPI.Controllers
{
    [Produces("application/json")]
    [Consumes("application/json")]
    [Route("api/[controller]")]
    [ApiController]
    public class ProfilesController : ControllerBase
    {
        private readonly WedlancerContext _context;
        public ProfilesController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Returns all freelancer profiles
        /// </summary>
        /// <returns></returns>
        //GET: api/Profiles/allprofiles
        [HttpGet("allprofiles")]
        public async Task<ActionResult<List<userdata>>> allprofiles()
        {
            UserRoles role = await _context.UserRoles.Where(r => r.Role.RoleName == "Freelancer")
                            .FirstOrDefaultAsync();

            var userprofiles = await (from p in _context.Profiles
                                      where p.IsActive == true
                                      && p.UserRoles.FirstOrDefault().RoleId == role.RoleId
                                      select new userdata
                                      {
                                          Id = p.ProfileId,
                                          ProfilePicture = p.ProfilePicture,
                                          UserName = p.Username,
                                          firstname = p.FirstName,
                                          lastname = p.LastName,
                                          Email = p.Email,
                                          category = p.Category.CategoryName,
                                          phonenumber = p.PhoneNumber,
                                          city = p.City.CityName,
                                          state = p.City.State.StateName,
                                          IsActive = p.IsActive
                                      }).ToListAsync();

            return userprofiles;
        }

        /// <summary>
        /// Returns the list of freelancer (only for Admin)
        /// </summary>
        /// <returns></returns>
        [Authorize("Admin")]
        [HttpGet("freelancers")]
        public async Task<ActionResult<List<userdata>>> freelancers()
        {

            UserRoles role = await _context.UserRoles.Where(r => r.Role.RoleName == "Freelancer")
                .FirstOrDefaultAsync();

            var userprofiles = await (from p in _context.Profiles
                                      where p.UserRoles.FirstOrDefault().RoleId == role.RoleId
                                      select new userdata
                                      {
                                          Id = p.ProfileId,
                                          ProfilePicture = p.ProfilePicture,
                                          UserName = p.Username,
                                          firstname = p.FirstName,
                                          lastname = p.LastName,
                                          Email = p.Email,
                                          category = p.Category.CategoryName,
                                          phonenumber = p.PhoneNumber,
                                          city = p.City.CityName,
                                          state = p.City.State.StateName,
                                          IsActive = p.IsActive
                                      }).ToListAsync();

            return userprofiles;
        }

        /// <summary>
        /// Returns the list of employers
        /// </summary>
        /// <returns></returns>
        [Authorize("Admin")]
        [HttpGet("employers")]
        public async Task<ActionResult<List<userdata>>> employers()
        {

            UserRoles role = await _context.UserRoles.Where(r => r.Role.RoleName == "Employeer ")
                .FirstOrDefaultAsync();

            var userprofiles = await (from p in _context.Profiles
                                      where p.UserRoles.FirstOrDefault().RoleId == role.RoleId
                                      select new userdata
                                      {
                                          Id = p.ProfileId,
                                          ProfilePicture = p.ProfilePicture,
                                          UserName = p.Username,
                                          firstname = p.FirstName,
                                          lastname = p.LastName,
                                          Email = p.Email,
                                          category = p.Category.CategoryName,
                                          phonenumber = p.PhoneNumber,
                                          city = p.City.CityName,
                                          state = p.City.State.StateName,
                                          IsActive = p.IsActive
                                      }).ToListAsync();

            return userprofiles;
        }

        /// <summary>
        /// Returns all freelancer profiles available in the given date range
        /// </summary>
        /// <param name="startdate"></param>
        /// <param name="enddate"></param>
        /// <param name="category"></param>
        /// <param name="city"></param>
        /// <returns></returns>
        //GET: api/Profiles/availableprofiles
        //?startdate=2022-05-16&enddate=2022-05-16
        [HttpGet("availableprofiles")]
        public async Task<ActionResult<List<userdata>>> availableprofiles(DateTime startdate, DateTime enddate, string category, string city)
        {
            UserRoles role = await _context.UserRoles.Where(r => r.Role.RoleName == "Freelancer")
                            .FirstOrDefaultAsync();

            var bookings = _context.Bookings.Where(
                b => b.StartDate.Date >= startdate.Date && b.EndDate.Date <= enddate.Date)
                .Select(p => p.ProfileId).ToArray();

            var Category = await _context.Categories.Where(c => c.CategoryName.Equals(category))
                .FirstOrDefaultAsync();

            var City = await _context.Cities.Where(c => c.CityName.Equals(city))
                .FirstOrDefaultAsync();

            var userprofiles = await (from p in _context.Profiles
                                where !bookings.Contains(p.ProfileId)
                                && p.IsActive == true 
                                && p.CityId == City.CityId
                                && p.CategoryId == Category.CategoryId
                                && p.UserRoles.FirstOrDefault().RoleId == role.RoleId
                                select new userdata
                                {
                                    Id = p.ProfileId,
                                    ProfilePicture = p.ProfilePicture,
                                    UserName = p.Username,
                                    firstname = p.FirstName,
                                    lastname = p.LastName,
                                    Email  = p.Email,
                                    category = p.Category.CategoryName,
                                    phonenumber = p.PhoneNumber,
                                    city = p.City.CityName,
                                    state = p.City.State.StateName,
                                    IsActive = p.IsActive
                                }).ToListAsync();
                                

            var profiles = await _context.Profiles.Where(p => !bookings.Contains(p.ProfileId))
                .ToListAsync();

            return userprofiles;
        }

        /// <summary>
        /// Searches for profiles matching the parameter
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        /// <response code="500">User not found</response>
        [HttpGet("searchprofile")]
        public async Task<ActionResult<List<userdata>>> searchprofile(string username)
        {
            var profile = await _context.Profiles.Where(p => p.Username == username && p.IsActive == true)
                .Select(p => new userdata
                {
                    Id = p.ProfileId,
                    ProfilePicture = p.ProfilePicture,
                    firstname = p.FirstName,
                    lastname = p.LastName,
                    UserName = p.Username,
                    category = p.Category.CategoryName,
                    city = p.City.CityName,
                    state = p.City.State.StateName,
                    Email = p.Email,
                    phonenumber = p.PhoneNumber,
                    IsActive = p.IsActive
                }).ToListAsync();

            if(profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            return profile;
        }

        /// <summary>
        /// Returns profile details of a particular user
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        /// <response code="500">User not found</response>
        [HttpGet("profiledetails")]
        public async Task<ActionResult<userdata>> profiledetails(string username)
        {
            var profile = await _context.Profiles.Where(p => p.Username == username)
                .Select(p => new userdata
                {
                    Id = p.ProfileId,
                    ProfilePicture = p.ProfilePicture,
                    firstname = p.FirstName,
                    lastname = p.LastName,
                    UserName = p.Username,
                    city = p.City.CityName,
                    state = p.City.State.StateName,
                    Email = p.Email,
                    phonenumber = p.PhoneNumber,
                    category = p.Category.CategoryName,
                    IsActive = p.IsActive
                }).FirstOrDefaultAsync();

            if(profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            return profile;
        }

        /// <summary>
        /// Returns all portfolios of the freelancer
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        [HttpGet("searchportfolio")]
        public async Task<ActionResult<List<freelancerportfolio>>> searchportfolio(string username)
        {
            var freelancer = await _context.Profiles
                .Where(f => f.Username == username)
                .FirstOrDefaultAsync();

            var portfolio = await _context.Portfolio
                .Where(p => p.ProfileId == freelancer.ProfileId)
                .Select(p => new freelancerportfolio
                {
                    portfolioId = p.PortfolioId,
                    username = freelancer.Username,
                    image = p.Image,
                    video = p.Video
                }).ToListAsync();

            return portfolio;
        }

        /// <summary>
        /// Updates user profile
        /// </summary>
        /// <param name="username"></param>
        /// <param name="profile"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        /// <response code="404">Not found</response>
        [HttpPut("updateprofile")]
        public async Task<IActionResult> updateprofile(string username,userdata profile)
        {
            if(profile.UserName != username)
            {
                return BadRequest();
            }

            var city = await _context.Cities.Where(c => c.CityName == profile.city)
                .FirstOrDefaultAsync();

            if(city == null)
            {
                return BadRequest(new { Status = "Error", Message = "City not found!" });
            }

            var state = await _context.States.Where(s => s.StateName == profile.state)
                .FirstOrDefaultAsync();

            if(state == null)
            {
                return BadRequest(new { Status = "Error", Message = "State not found!" });
            }

            var category = await _context.Categories.Where(c => c.CategoryName == profile.category)
                .FirstOrDefaultAsync();

            if(category == null)
            {
                return BadRequest(new { Status = "Error", Message = "category not found!" });
            }

            var Profile = await _context.Profiles.Where(p => p.Username == username)
                .FirstOrDefaultAsync();

            if(Profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            Profile.FirstName = profile.firstname;
            Profile.LastName = profile.lastname;
            Profile.ProfilePicture = profile.ProfilePicture;
            Profile.PhoneNumber = profile.phonenumber;
            Profile.Email = profile.Email;
            Profile.CityId = city.CityId;
            Profile.CategoryId = category.CategoryId;

            _context.Entry(Profile).State = EntityState.Modified;
            await _context.SaveChangesAsync();

            return Ok(new { Status = "Success", Message = "Profile details Changed!" });
        }

        /// <summary>
        /// Activates or deactivates the user account (only for Admin)
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        /// <response code="500">User not found</response>
        [Authorize("Admin")]
        [HttpPut("changeaccountstatus")]
        public async Task<IActionResult> changeaccountstatus(string username)
        {
            var profile = await _context.Profiles
                .Where(p => p.Username == username)
                .FirstOrDefaultAsync();


            if(profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            if(profile.IsActive)
            {
                profile.IsActive = false;
            }
            else
            {
                profile.IsActive = true;
            }

            _context.Entry(profile).State = EntityState.Modified;
            await _context.SaveChangesAsync();

            return Ok(new {Status = "Success", Message = "Profile Status Changed!" });
        }

        /// <summary>
        /// Returns all booking of the freelancer (only for Freelancer and Admin)
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        /// <response code="500">Freelancer not found</response>
        /// <response code="204">No bookings</response>
        [AuthorizeMultiplePolicy("Freelancer,Admin")]
        [HttpGet("freelancerbookings")]
        public async Task<ActionResult<List<bookingdata>>> freelancerbookings(string username)
        {
            var profile = await _context.Profiles.Where(p => p.Username == username)
                .FirstOrDefaultAsync();

            if (profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            var bookings = await _context.Bookings.Where(p => p.ProfileId == profile.ProfileId)
                .Select(b => new bookingdata
                {
                    BookingId = b.BookingId,
                    freelancerusername = b.Profile.Username,
                    employerusername = b.Employer.Username,
                    StartDate = b.StartDate,
                    EndDate = b.EndDate,
                    Status = b.Status
                })
                .ToListAsync();

            if (bookings == null)
            {
                return NoContent();
            }

            return bookings;
        }

        /// <summary>
        /// Returns all bookings of an employer (only for Employer)
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        /// <response code="500">Employer not found</response>
        /// <response code="204">No bookings</response>
        [Authorize("Employeer")]
        [HttpGet("employeerbookings")]
        public async Task<ActionResult<List<bookingdata>>> employeerbookings(string username)
        {
            var profile = await _context.Profiles.Where(p => p.Username == username)
                .FirstOrDefaultAsync();

            if (profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            var bookings = await _context.Bookings.Where(p => p.EmployerId == profile.ProfileId)
                .Select(b => new bookingdata
                {
                    BookingId = b.BookingId,
                    freelancerusername = b.Profile.Username,
                    employerusername = b.Employer.Username,
                    StartDate = b.StartDate,
                    EndDate = b.EndDate,
                    Status = b.Status
                })
                .ToListAsync();

            if (bookings == null)
            {
                return NoContent();
            }

            return bookings;
        }
    }
}
