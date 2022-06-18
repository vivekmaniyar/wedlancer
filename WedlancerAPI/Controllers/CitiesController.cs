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
    public class CitiesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public CitiesController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Returns all cities
        /// </summary>
        /// <returns>All cities</returns>
        // GET: api/Cities
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Cities>>> GetCities()
        {
            return await _context.Cities.Include(c => c.State).ToListAsync();
        }

        /// <summary>
        /// Returns a single city associated with the given ID
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Single city</returns>
        /// <response code="404">City not found</response>
        // GET: api/Cities/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Cities>> GetCities(int id)
        {
            var cities = await _context.Cities.Include(c => c.State)
                .FirstOrDefaultAsync(c => c.CityId == id);

            if (cities == null)
            {
                return NotFound();
            }

            return cities;
        }

        /// <summary>
        /// Updates a single city data (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <param name="cities"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="500">City ID is not matching</response>
        /// <response code="404">City not found</response>
        // PUT: api/Cities/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCities(int id, Cities cities)
        {
            if (id != cities.CityId)
            {
                return BadRequest();
            }

            _context.Entry(cities).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CitiesExists(id))
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
        /// Adds a new city (only for Admin)
        /// </summary>
        /// <param name="cities"></param>
        /// <returns>Newly added city</returns>
        // POST: api/Cities
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPost]
        public async Task<ActionResult<Cities>> PostCities(Cities cities)
        {
            _context.Cities.Add(cities);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetCities", new { id = cities.CityId }, cities);
        }

        /// <summary>
        /// Deletes a particular city
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Deleted city data</returns>
        /// <response code="404">City not found</response>
        // DELETE: api/Cities/5
        [Authorize("Admin")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Cities>> DeleteCities(int id)
        {
            var cities = await _context.Cities
                .Include(c => c.State).FirstOrDefaultAsync(c => c.CityId == id);
            if (cities == null)
            {
                return NotFound();
            }

            _context.Cities.Remove(cities);
            await _context.SaveChangesAsync();

            return cities;
        }

        /// <summary>
        /// Searchs for the city matching the parameter 
        /// </summary>
        /// <param name="name"></param>
        /// <returns>cities</returns>
        /// <response code="404">City not found</response>
        //GET: api/Cities/searchcity?name=Surat 
        [HttpGet("searchcity")]
        public async Task<ActionResult<Cities>> searchcity(String name)
        {
            var cities = await _context.Cities.Include(c => c.State)
                .FirstOrDefaultAsync(c => c.CityName.Contains(name));

            if (cities == null)
            {
                return NotFound();
            }

            return cities;
        }

        /// <summary>
        /// Search for the cities in a particular state
        /// </summary>
        /// <param name="state"></param>
        /// <returns>Cities</returns>
        /// <response code="404">Cities not found</response>
        //GET: api/Cities/searchbystate?state=Gujarat
        [HttpGet("searchbystate")]
        public async Task<ActionResult<IEnumerable<Cities>>> searchbystate(String state)
        {
            var cities = await _context.Cities.Include(c => c.State)
                .Where(c => c.State.StateName.Equals(state)).ToListAsync();

            if (cities == null)
            {
                return NotFound();
            }

            return cities;
        }

        private bool CitiesExists(int id)
        {
            return _context.Cities.Any(e => e.CityId == id);
        }
    }
}
