using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WedlancerAPI.Authorize;
using WedlancerAPI.Models;

namespace WedlancerAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReviewsController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public ReviewsController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/Reviews
        [HttpGet]
        public async Task<ActionResult<IEnumerable<userreview>>> GetReviews()
        {
            var reviews = await (from r in _context.Reviews
                           select new userreview
                           {
                               employeerusername = r.Profile.Username,
                               freelancerusername = r.Freelancer.Username,
                               rating = r.Stars,
                               message = r.Message
                           }).ToListAsync();

            return reviews;
        }

        // GET: api/Reviews/5
        [HttpGet("{id}")]
        public async Task<ActionResult<userreview>> GetReviews(int id)
        {
            var reviews = await _context.Reviews
                .Include(e => e.Profile)
                .Include(f => f.Freelancer)
                .Where(r => r.ReviewId == id)
                .FirstOrDefaultAsync();

            var review = await (from r in _context.Reviews
                          where r.ReviewId == id
                          select new userreview
                          {
                              employeerusername = r.Profile.Username,
                              freelancerusername = r.Freelancer.Username,
                              rating = r.Stars,
                              message = r.Message
                          }).FirstOrDefaultAsync();

            if (review == null)
            {
                return NotFound();
            }

            return review;
        }

        // PUT: api/Reviews/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Employeer")]
        [HttpPut]
        public async Task<IActionResult> PutReviews(string username, userreview updatedreview)
        {
            if (username != updatedreview.freelancerusername)
            {
                return BadRequest();
            }

            var freelancer = await _context.Profiles
                .Where(f => f.Username == updatedreview.freelancerusername)
                .FirstOrDefaultAsync();

            var employer = await _context.Profiles
                .Where(e => e.Username == updatedreview.employeerusername)
                .FirstOrDefaultAsync();

            var reviews = await _context.Reviews
                .Where(r => r.FreelancerId == freelancer.ProfileId
                 && r.ProfileId == employer.ProfileId)
                .FirstOrDefaultAsync();

            reviews.Message = updatedreview.message;
            reviews.Stars = updatedreview.rating;

            _context.Entry(reviews).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ReviewsExists(reviews.ReviewId))
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

        // POST: api/Reviews
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Employeer")]
        [HttpPost]
        public async Task<ActionResult<Reviews>> PostReviews(userreview newreview)
        {
            var freelancer = await _context.Profiles
                .Where(f => f.Username == newreview.freelancerusername)
                .FirstOrDefaultAsync();

            var employer = await _context.Profiles
                .Where(e => e.Username == newreview.employeerusername)
                .FirstOrDefaultAsync();

            Reviews reviews = new Reviews();
            reviews.Stars = newreview.rating;
            reviews.Message = newreview.message;
            reviews.FreelancerId = freelancer.ProfileId;
            reviews.ProfileId = employer.ProfileId;
            _context.Reviews.Add(reviews);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetReviews", new { Status="Success", Message="New review added successfully!" });
        }

        // DELETE: api/Reviews/5
        [Authorize("Employeer")]
        [HttpDelete]
        public async Task<IActionResult> DeleteReviews(string freelancer,string employer)
        {
            var Freelancer = await _context.Profiles
                .Where(f => f.Username == freelancer)
                .FirstOrDefaultAsync();

            var Employer = await _context.Profiles
                .Where(e => e.Username == employer)
                .FirstOrDefaultAsync();

            var reviews = await _context.Reviews
                .Where(u => u.FreelancerId == Freelancer.ProfileId
                 && u.ProfileId == Employer.ProfileId)
                .FirstOrDefaultAsync();

            if (reviews == null)
            {
                return NotFound();
            }

            _context.Reviews.Remove(reviews);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        [Authorize("Employeer")]
        [HttpGet("reviewsbyemployer")]
        public async Task<ActionResult<IEnumerable<userreview>>> reviewsbyemployer(string username)
        {
            var employer = await _context.Profiles
                .Where(e => e.Username == username)
                .FirstOrDefaultAsync();

            var reviews = await _context.Reviews
                .Include(r => r.Freelancer)
                .Include(r => r.Profile)
                .Where(u => u.ProfileId == employer.ProfileId)
                .Select(r => new userreview
                {
                    freelancerusername = r.Freelancer.Username,
                    employeerusername = r.Profile.Username,
                    rating = r.Stars,
                    message = r.Message
                }).ToListAsync();

            return reviews;
        }

        [HttpGet("freelancerreviews")]
        public async Task<ActionResult<IEnumerable<userreview>>> freelancerreviews(string username)
        {
            var freelancer = await _context.Profiles
                .Where(f => f.Username == username)
                .FirstOrDefaultAsync();

            var reviews = await _context.Reviews
                .Include(r => r.Freelancer)
                .Include(r => r.Profile)
                .Where(u => u.FreelancerId == freelancer.ProfileId)
                .Select(r => new userreview
                {
                    freelancerusername = r.Freelancer.Username,
                    employeerusername = r.Profile.Username,
                    rating = r.Stars,
                    message = r.Message
                }).ToListAsync();

            return reviews;
        }

        private bool ReviewsExists(int id)
        {
            return _context.Reviews.Any(e => e.ReviewId == id);
        }
    }
}
