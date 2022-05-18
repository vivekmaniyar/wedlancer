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
    public class StatesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public StatesController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/States
        [HttpGet]
        public async Task<ActionResult<IEnumerable<States>>> GetStates()
        {
            return await _context.States
                .Select(s => new States
                {
                    StateId = s.StateId,
                    StateName = s.StateName
                }).ToListAsync();
                
        }

        // GET: api/States/5
        [HttpGet("{id}")]
        public async Task<ActionResult<States>> GetStates(int id)
        {

            var states = await _context.States
                .Select(s => new States
                {
                    StateId = s.StateId,
                    StateName = s.StateName
                }).Where(s => s.StateId == id)
                .FirstOrDefaultAsync();

            if (states == null)
            {
                return NotFound();
            }

            return states;
        }

        // PUT: api/States/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutStates(int id, States states)
        {
            if (id != states.StateId)
            {
                return BadRequest();
            }

            _context.Entry(states).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StatesExists(id))
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

        // POST: api/States
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPost]
        public async Task<ActionResult<States>> PostStates(States states)
        {
            _context.States.Add(states);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetStates", new { id = states.StateId }, states);
        }

        // DELETE: api/States/5
        [Authorize("Admin")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<States>> DeleteStates(int id)
        {
            var states = await _context.States.FindAsync(id);
            if (states == null)
            {
                return NotFound();
            }

            _context.States.Remove(states);
            await _context.SaveChangesAsync();

            return states;
        }

        //GET: api/States/searchstate?name=Gujarat
        [HttpGet("searchstate")]
        public async Task<ActionResult<States>> searchstate(String name)
        {
            var states = await _context.States.FirstOrDefaultAsync(s => s.StateName.Contains(name));

            if (states == null)
            {
                return NotFound();
            }

            return states;
        }

        private bool StatesExists(int id)
        {
            return _context.States.Any(e => e.StateId == id);
        }
    }
}
