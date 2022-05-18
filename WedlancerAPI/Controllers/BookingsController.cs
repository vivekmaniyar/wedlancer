using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WedlancerAPI.Models;

namespace WedlancerAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BookingsController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public BookingsController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/Bookings
        [HttpGet]
        public async Task<ActionResult<IEnumerable<bookingdata>>> GetBookings()
        {
         

            var bookings = await _context.Bookings
                .Select(b => new bookingdata
                {
                    BookingId = b.BookingId,
                    freelancerusername = b.Profile.Username,
                    employerusername = b.Employer.Username,
                    StartDate = b.StartDate,
                    EndDate = b.EndDate,
                    Status = b.Status
                }).ToListAsync();

            return bookings;
        }

        // GET: api/Bookings/5
        [HttpGet("{id}")]
        public async Task<ActionResult<bookingdata>> GetBookings(int id)
        {
            var bookings = await _context.Bookings
                .Where(b => b.BookingId == id)
                .Select(b => new bookingdata
                {
                    BookingId = b.BookingId,
                    freelancerusername = b.Profile.Username,
                    employerusername = b.Employer.Username,
                    StartDate = b.StartDate,
                    EndDate = b.EndDate,
                    Status = b.Status
                }).FirstOrDefaultAsync();

            if (bookings == null)
            {
                return NotFound();
            }

            return bookings;
        }

        // PUT: api/Bookings/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPut("{id}")]
        public async Task<IActionResult> PutBookings(int id, string status)
        {
            var bookings = await _context.Bookings
                .FirstOrDefaultAsync(b => b.BookingId == id);

            if (id != bookings.BookingId)
            {
                return BadRequest();
            }

            bookings.Status = status;

            _context.Entry(bookings).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!BookingsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Bookings
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Freelancer")]
        [Authorize("Employeer")]
        [HttpPost]
        public async Task<ActionResult<bookingdata>> PostBookings(bookingdata newbooking)
        {
            var freelancer = await _context.Profiles
                .FirstOrDefaultAsync(f => f.Username == newbooking.freelancerusername);

            var employer = await _context.Profiles
                .FirstOrDefaultAsync(e => e.Username == newbooking.employerusername);

            Bookings bookings = new Bookings();
            bookings.ProfileId = freelancer.ProfileId;
            bookings.EmployerId = employer.ProfileId;
            bookings.StartDate = newbooking.StartDate;
            bookings.EndDate = newbooking.EndDate;
            bookings.Status = "Pending";

            _context.Bookings.Add(bookings);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetBookings", new { status="Success", Message="Successfully booked!" });
        }

        

        private bool BookingsExists(int id)
        {
            return _context.Bookings.Any(e => e.BookingId == id);
        }
    }
}
