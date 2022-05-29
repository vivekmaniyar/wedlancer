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
    public class OrdersController : ControllerBase
    {
        private readonly WedlancerContext _context;

        public OrdersController(WedlancerContext context)
        {
            _context = context;
        }

        // GET: api/Orders
        [HttpGet]
        public async Task<ActionResult<IEnumerable<orderdetails>>> GetOrders()
        {
            var orders = await _context.Orders.Include(o => o.Package).Include(o => o.Freelancer)
                .Select(o => new orderdetails
                {
                    OrderId = o.OrderId,
                    PaymentId = o.PaymentId,
                    Amount = o.Amount,
                    package = o.Package.PackageName,
                    freelancerusername = o.Freelancer.Username,
                    status = o.Status
                }).ToListAsync();

            return orders;
        }

        // GET: api/Orders/5
        [HttpGet("{id}")]
        public async Task<ActionResult<orderdetails>> GetOrders(int id)
        {
            var orders = await _context.Orders.Include(o => o.Package).Include(o => o.Freelancer)
                .Select(o => new orderdetails
                {
                    OrderId = o.OrderId,
                    PaymentId = o.PaymentId,
                    Amount = o.Amount,
                    package = o.Package.PackageName,
                    freelancerusername = o.Freelancer.Username,
                    status = o.Status
                }).FirstOrDefaultAsync(o => o.OrderId == id);

            if (orders == null)
            {
                return NotFound();
            }

            return orders;
        }

        [Authorize("Freelancer")]
        [HttpGet("freelancerorders")]
        public async Task<ActionResult<List<orderdetails>>> freelancerorders(string username)
        {
            var profile = await _context.Profiles.Where(p => p.Username == username)
                .FirstOrDefaultAsync();

            if (profile == null)
            {
                return BadRequest(new { Status = "Error", Message = "User not found!" });
            }

            var orders = await _context.Orders.Include(o => o.Package).Include(o => o.Freelancer)
                .Where(p => p.FreelancerId == profile.ProfileId)
                .Select(o => new orderdetails
                {
                    OrderId = o.OrderId,
                    PaymentId = o.PaymentId,
                    Amount = o.Amount,
                    package = o.Package.PackageName,
                    freelancerusername = o.Freelancer.Username,
                    status = o.Status
                }).ToListAsync();

            return orders;
        }

        // PUT: api/Orders/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Admin")]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutOrders(int id, string status)
        {
            var orders = await _context.Orders.FindAsync(id);
            if (id != orders.OrderId)
            {
                return BadRequest();
            }

            orders.Status = status;
            _context.Entry(orders).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!OrdersExists(id))
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

        // POST: api/Orders
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize("Freelancer")]
        [HttpPost]
        public async Task<ActionResult<Orders>> PostOrders(orderdetails neworder)
        {
            var freelancer = await _context.Profiles
                .Where(f => f.Username == neworder.freelancerusername)
                .FirstOrDefaultAsync();

            var package = await _context.Packages
                .Where(p => p.PackageName == neworder.package)
                .FirstOrDefaultAsync();

            Orders orders = new Orders();
            orders.PaymentId = neworder.PaymentId;
            orders.FreelancerId = freelancer.ProfileId;
            orders.Amount = neworder.Amount;
            orders.PackageId = package.PackageId;
            orders.Status = neworder.status;

            _context.Orders.Add(orders);
            await _context.SaveChangesAsync();
            neworder.OrderId = orders.OrderId;

            return CreatedAtAction("GetOrders", new { id = orders.OrderId }, neworder);
        }

        private bool OrdersExists(int id)
        {
            return _context.Orders.Any(e => e.OrderId == id);
        }
    }
}
