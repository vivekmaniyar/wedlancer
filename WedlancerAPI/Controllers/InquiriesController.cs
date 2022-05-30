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
    public class InquiriesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public InquiriesController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/Inquiries
        [Authorize("Admin")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Inquiries>>> GetInquiries()
        {
            return await _context.Inquiries.ToListAsync();
        }

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

        // POST: api/Inquiries
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<Inquiries>> PostInquiries(Inquiries inquiries)
        {
            _context.Inquiries.Add(inquiries);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetInquiries", new { id = inquiries.Id }, inquiries);
        }

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
