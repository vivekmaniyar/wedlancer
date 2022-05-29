﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WedlancerAPI.Models;

namespace WedlancerAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProfilesController : ControllerBase
    {
        private readonly WedlancerContext _context;
        public ProfilesController(WedlancerContext context)
        {
            _context = context;
        }

        //GET: api/Profiles/allprofiles
        [HttpGet("allprofiles")]
        public async Task<ActionResult<List<userdata>>> allprofiles()
        {

            var userprofiles = await (from p in _context.Profiles
                                      where p.IsActive == true
                                      select new userdata
                                      {
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

            var profiles = await _context.Profiles
                .Include(p => p.UserRoles)
                .Include(p => p.Category)
                .Include(p => p.City)
                .Include(p => p.City.State)
                .Where(p => p.IsActive == true)
                .ToListAsync();

            return userprofiles;
        }

        //GET: api/Profiles/availableprofiles
        //?startdate=2022-05-16T06:53:58.133Z&enddate=2022-05-16T06:53:58.133Z
        [HttpGet("availableprofiles")]
        public async Task<ActionResult<List<userdata>>> availableprofiles(DateTime startdate, DateTime enddate, string category, string city)
        {
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
                                select new userdata
                                {
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

        [Authorize("Freelancer")]
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
