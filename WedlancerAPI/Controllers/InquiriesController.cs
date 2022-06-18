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
    [Produces("application/json")]
    [Consumes("application/json")]
    [Route("api/[controller]")]
    [ApiController]
    public class InquiriesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public InquiriesController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Returns all inquiries (only for Admin)
        /// </summary>
        /// <returns>All inquiries</returns>
        // GET: api/Inquiries
        [Authorize("Admin")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Inquiries>>> GetInquiries()
        {
            return await _context.Inquiries.ToListAsync();
        }

        /// <summary>
        /// Returns data of a particular inquiry (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Inquiry data</returns>
        /// <response code="404">Inquiry not found</response>
        // GET: api/Inquiries/5
        [Authorize("Admin")]
        [HttpGet("{id}")]
        public async Task<ActionResult<Inquiries>> GetInquiries(int id)
        {
            var inquiries = await _context.Inquiries.FindAsync(id);

            if (inquiries == null)
            {
                return NotFound();
            }

            return inquiries;
        }

        /// <summary>
        /// Updates status of an inquiry (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="500">Inquiry ID not matching</response>
        /// <response code="404">Inquiry not found</response>
        // PUT: api/Inquiries/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutInquiries(int id, string status)
        {
            var inquiries = await _context.Inquiries
                .FirstOrDefaultAsync(i => i.Id == id);

            if (id != inquiries.Id)
            {
                return BadRequest();
            }

            inquiries.Status = status;
            _context.Entry(inquiries).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!InquiriesExists(id))
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

        /// <summary>
        /// Adds a new inquiry
        /// </summary>
        /// <param name="inquiries"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        // POST: api/Inquiries
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<Inquiries>> PostInquiries(Inquiries inquiries)
        {
            inquiries.Status = "Pending";
            _context.Inquiries.Add(inquiries);
            await _context.SaveChangesAsync();

            return Ok(new {Status="Success",Message="Inquiry added successfully"});
        }

        /// <summary>
        /// Deletes a particular inquiry (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Deleted inquiry</returns>
        /// <response code="404">Inquiry not found</response>
        // DELETE: api/Inquiries/5
        [Authorize("Admin")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Inquiries>> DeleteInquiries(int id)
        {
            var inquiries = await _context.Inquiries.FindAsync(id);
            if (inquiries == null)
            {
                return NotFound();
            }

            _context.Inquiries.Remove(inquiries);
            await _context.SaveChangesAsync();

            return inquiries;
        }

        private bool InquiriesExists(int id)
        {
            return _context.Inquiries.Any(e => e.Id == id);
        }
    }
}
