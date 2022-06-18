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
    public class PackagesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public PackagesController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Returns all packages
        /// </summary>
        /// <returns>All packages</returns>
        // GET: api/Packages
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Packages>>> GetPackages()
        {
            return await _context.Packages.ToListAsync();
        }

        /// <summary>
        /// Returns package detail of a given ID
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Package details</returns>
        /// <response code="404">Package not found</response>
        // GET: api/Packages/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Packages>> GetPackages(int id)
        {
            var packages = await _context.Packages.FindAsync(id);

            if (packages == null)
            {
                return NotFound();
            }

            return packages;
        }

        /// <summary>
        /// Updates package details (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <param name="package"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="500">Package ID not matching</response>
        /// <response code="404">Package not found</response>
        // PUT: api/Packages/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutPackages(int id, Packages package)
        {

            if (id != package.PackageId)
            {
                return BadRequest();
            }
            _context.Entry(package).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PackagesExists(id))
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
        /// Adds a new package (only for Admin)
        /// </summary>
        /// <param name="package"></param>
        /// <returns></returns>
        // POST: api/Packages
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPost]
        public async Task<ActionResult<Packages>> PostPackages(Packages package)
        {
            _context.Packages.Add(package);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPackages", new { Status="Success", Message="Package successfully added!" });
        }

        /// <summary>
        /// Deletes the package (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        /// <response code="404">Package not found</response>
        // DELETE: api/Packages/5
        [Authorize("Admin")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Packages>> DeletePackages(int id)
        {
            var packages = await _context.Packages.FindAsync(id);
            if (packages == null)
            {
                return NotFound();
            }

            _context.Packages.Remove(packages);
            await _context.SaveChangesAsync();

            return packages;
        }

        private bool PackagesExists(int id)
        {
            return _context.Packages.Any(e => e.PackageId == id);
        }
    }
}
