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
    public class PortfoliosController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public PortfoliosController(WedlancerContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Return all portfolios
        /// </summary>
        /// <returns></returns>
        // GET: api/Portfolios
        [HttpGet]
        public async Task<ActionResult<IEnumerable<freelancerportfolio>>> GetPortfolio()
        {
            return await _context.Portfolio.Include(p => p.Profile)
                .Select(p => new freelancerportfolio
                {
                    portfolioId = p.PortfolioId,
                    username = p.Profile.Username,
                    image = p.Image,
                    video = p.Video
                }).ToListAsync();
        }

        /// <summary>
        /// Returns a particular portfolio
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Portfolio</returns>
        /// <response code="404">Portfolio not found</response>
        // GET: api/Portfolios/5
        [HttpGet("{id}")]
        public async Task<ActionResult<freelancerportfolio>> GetPortfolio(int id)
        {
            var portfolio = await _context.Portfolio
                .Include(p => p.Profile).Where(p => p.PortfolioId == id)
                .Select(p => new freelancerportfolio
                {
                    portfolioId = p.PortfolioId,
                    username = p.Profile.Username,
                    image = p.Image,
                    video = p.Video
                })
                .FirstOrDefaultAsync();

            if (portfolio == null)
            {
                return NotFound();
            }

            return portfolio;
        }

        /// <summary>
        /// Updates the portfolio (only for Freelancer)
        /// </summary>
        /// <param name="id"></param>
        /// <param name="updatedportfolio"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="500">Portfolio ID not matching</response>
        /// <response code="404">Portfolio not found</response>
        // PUT: api/Portfolios/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPut("{id}")]
        [Authorize("Freelancer")]
        public async Task<IActionResult> PutPortfolio(int id, freelancerportfolio updatedportfolio)
        {
            var freelancer = await _context.Profiles
                .Where(p => p.Username == updatedportfolio.username)
                .FirstOrDefaultAsync();

            var portfolio = await _context.Portfolio
                .Where(p => p.ProfileId == freelancer.ProfileId)
                .FirstOrDefaultAsync();

            if (id != updatedportfolio.portfolioId)
            {
                return BadRequest();
            }

            portfolio.Image = updatedportfolio.image;
            portfolio.Video = updatedportfolio.video;
            _context.Entry(portfolio).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PortfolioExists(id))
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
        /// Adds a new portfolio (only for Freelancer)
        /// </summary>
        /// <param name="newportfolio"></param>
        /// <returns></returns>
        // POST: api/Portfolios
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Freelancer")]
        [HttpPost]
        public async Task<ActionResult<freelancerportfolio>> PostPortfolio(freelancerportfolio newportfolio)
        {
            var freelancer = await _context.Profiles
                .Where(p => p.Username == newportfolio.username)
                .FirstOrDefaultAsync();

            Portfolio portfolio = new Portfolio();
            portfolio.ProfileId = freelancer.ProfileId;
            portfolio.Image = newportfolio.image;
            portfolio.Video = newportfolio.video;

            _context.Portfolio.Add(portfolio);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetPortfolio", new { Status="Success",Message="Portfolio Added Successfully!" });
        }

        /// <summary>
        /// Deletes the portfolio (only for Freelancer)
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        /// <response code="204">Success</response>
        /// <response code="404">Portfolio not found</response>
        // DELETE: api/Portfolios/5
        [Authorize("Freelancer")]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Portfolio>> DeletePortfolio(int id)
        {
            var portfolio = await _context.Portfolio.FindAsync(id);
            if (portfolio == null)
            {
                return NotFound();
            }

            _context.Portfolio.Remove(portfolio);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool PortfolioExists(int id)
        {
            return _context.Portfolio.Any(e => e.PortfolioId == id);
        }
    }
}
