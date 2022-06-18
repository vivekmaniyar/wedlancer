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
    public class CategoriesController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public CategoriesController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Return all categories
        /// </summary>
        /// <returns>All categories</returns>
        // GET: api/Categories
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Categories>>> GetCategories()
        {


            return await _context.Categories.ToListAsync();
        }

        /// <summary>
        /// Returns a single category associated with the given ID
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Single category</returns>
        /// <response code="404">Category not found</response>
        // GET: api/Categories/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Categories>> GetCategories(int id)
        {
            var categories = await _context.Categories
                .Where(c => c.CategoryId == id)
                .FirstOrDefaultAsync();

            if (categories == null)
            {
                return NotFound();
            }

            return categories;
        }

        /// <summary>
        /// Updates a category (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <param name="categories"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="500">Category ID not matching</response>
        /// <response code="404">Category not found</response>
        // PUT: api/Categories/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCategories(int id, Categories categories)
        {
            if (id != categories.CategoryId)
            {
                return BadRequest();
            }

            _context.Entry(categories).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CategoriesExists(id))
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
        /// Adds a new category (only for Admin)
        /// </summary>
        /// <param name="categories"></param>
        /// <returns>New category data</returns>
        // POST: api/Categories
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPost]
        public async Task<ActionResult<Categories>> PostCategories(Categories categories)
        {
            _context.Categories.Add(categories);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetCategories", new { id = categories.CategoryId }, categories);
        }

        /// <summary>
        /// Deletes a particular category (only for Admin)
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Deleted category</returns>
        // DELETE: api/Categories/5
        [Authorize("Admin")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Categories>> DeleteCategories(int id)
        {
            var categories = await _context.Categories
                .Select(c => new Categories
                {
                    CategoryId = c.CategoryId,
                    CategoryName = c.CategoryName
                }).Where(c => c.CategoryId == id)
                .FirstOrDefaultAsync();

            if (categories == null)
            {
                return NotFound();
            }

            _context.Categories.Remove(categories);
            await _context.SaveChangesAsync();

            return categories;
        }

        /// <summary>
        /// Searches for categories matching the parameter
        /// </summary>
        /// <param name="name"></param>
        /// <returns>categories</returns>
        /// <response code="404">Not found</response>
        //GET: api/Categories/searchcategory?name=Photographer
        [HttpGet("searchcategory")]
        public async Task<ActionResult<Categories>> searchcategory(String name)
        {
            var categories = await _context.Categories
                .Select(c => new Categories
                {
                    CategoryId = c.CategoryId,
                    CategoryName = c.CategoryName
                })
                .FirstOrDefaultAsync(c => c.CategoryName.Contains(name));

            if (categories == null)
            {
                return NotFound();
            }

            return categories;
        }

        private bool CategoriesExists(int id)
        {
            return _context.Categories.Any(e => e.CategoryId == id);
        }
    }
}
