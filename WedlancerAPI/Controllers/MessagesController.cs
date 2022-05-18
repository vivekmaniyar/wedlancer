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
    [AuthorizeMultiplePolicy("Freelancer,Employeer")]
    [Route("api/[controller]")]
    [ApiController]
    public class MessagesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public MessagesController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/Messages
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Messages>>> GetMessages()
        {
            return await _context.Messages
                .Include(e => e.Employer)
                .Include(e => e.Employer.City)
                .Include(e => e.Employer.City.State)
                .Include(e => e.Employer.Category)
                .Include(f => f.Freelancer)
                .Include(f => f.Freelancer.City)
                .Include(f => f.Freelancer.City.State)
                .Include(f => f.Freelancer.Category)
                .ToListAsync();
        }

        // GET: api/Messages/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Messages>> GetMessages(int id)
        {
            var messages = await _context.Messages
                .Include(e => e.Employer)
                .Include(e => e.Employer.City)
                .Include(e => e.Employer.City.State)
                .Include(e => e.Employer.Category)
                .Include(f => f.Freelancer)
                .Include(f => f.Freelancer.City)
                .Include(f => f.Freelancer.City.State)
                .Include(f => f.Freelancer.Category)
                .Where(m => m.MessageId == id)
                .FirstOrDefaultAsync();

            if (messages == null)
            {
                return NotFound();
            }

            return messages;
        }


        // POST: api/Messages
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<Messages>> PostMessages(chats newmessage)
        {
            var freelancer = await _context.Profiles
                .FirstOrDefaultAsync
                (f => f.Username == newmessage.freelancerusername);

            var employer = await  _context.Profiles
                .FirstOrDefaultAsync
                (e => e.Username == newmessage.employerusername);

            Messages messages = new Messages();
            messages.FreelancerId = freelancer.ProfileId;
            messages.EmployerId = employer.ProfileId;
            messages.Message = newmessage.message;
            messages.CreatedOn = DateTime.Now;
            
            _context.Messages.Add(messages);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetMessages", new { id = messages.MessageId }, newmessage);
        }

        private bool MessagesExists(int id)
        {
            return _context.Messages.Any(e => e.MessageId == id);
        }
    }
}
