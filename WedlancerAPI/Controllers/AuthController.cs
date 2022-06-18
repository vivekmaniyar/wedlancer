using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using WedlancerAPI.Models;

namespace WedlancerAPI.Controllers
{
    [Produces("application/json")]
    [Consumes("application/json")]
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        private readonly IConfiguration _configuration;
        private readonly WedlancerContext _context;

        public AuthController(IConfiguration configuration, WedlancerContext context)
        {
            _configuration = configuration;
            _context = context;
        }
        /// <summary>
        /// Verifies user details and returns a JWT token after successful verification
        /// </summary>
        /// <param name="profile"></param>
        /// <returns>JWT token</returns>
        /// <response code="200">Successful verification</response>
        /// <response code="500">Profile is not active</response>
        /// <response code="401">Wrong user details</response>
        [HttpPost("login")]
        public async Task<IActionResult> login([FromBody]login profile)
        {
            var user = await _context.Profiles
                .FirstOrDefaultAsync(user => user.Username == profile.Username);

            if(user.IsActive == false)
            {
                return BadRequest(new { status = "Error", message = "Your profile is not active" });
            }

            if(user!=null && BCrypt.Net.BCrypt.Verify(profile.Password,user.Password))
            {

                var roles = await _context.UserRoles.Where(r => r.ProfileId == user.ProfileId)
                    .Include(r => r.Role)
                    .ToListAsync();
                var authclaims = new List<Claim>
                {
                    new Claim(ClaimTypes.Name,user.Username),
                    new Claim(JwtRegisteredClaimNames.Jti,Guid.NewGuid().ToString()),
                };

                foreach(var role in roles)
                {
                    authclaims.Add(new Claim(ClaimTypes.Role,role.Role.RoleName));
                }

                var token = gettoken(authclaims);

                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    expiration = token.ValidTo
                });
            }
            return Unauthorized();
        }
        /// <summary>
        /// Registers new admin 
        /// </summary>
        /// <param name="newuser"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        /// <response code="500">Admin with the given username already exists</response>
        [HttpPost("registeradmin")]
        public async Task<IActionResult> registeradmin([FromBody]register newuser)
        {
            var userexists = await _context.Profiles
                .Where(p => p.Username == newuser.Username).FirstOrDefaultAsync();

            var category = await _context.Categories
                .Where(c => c.CategoryName == newuser.category).FirstOrDefaultAsync();

            var city = await _context.Cities
                .Where(c => c.CityName == newuser.city).FirstOrDefaultAsync();

            if(userexists!=null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError,
                    new {Status = "Error", Message = "Admin already exists!"});
            }

            Profiles newprofile = new Profiles
            {
                Username = newuser.Username,
                FirstName = newuser.FirstName,
                LastName = newuser.LastName,
                Password = BCrypt.Net.BCrypt.HashPassword(newuser.Password),
                Email = newuser.Email,
                PhoneNumber = newuser.PhoneNumber,
                UserSince = DateTime.Now.Date,
                CategoryId = category.CategoryId,
                CityId = city.CityId,
                EmailConfirmed = true,
                PhoneNumberConfirmed = true,
                IsActive = true

            };

            var last = _context.Profiles.AsEnumerable().Last();

            newprofile.UserRoles.Add(new UserRoles
            {
                ProfileId = last.ProfileId + 1,
                RoleId = 1,
            });

            _context.Profiles.Add(newprofile);
            _context.SaveChanges();

            return Ok(new { Status = "Success", Message = "Admin Successfully registered!" });
        }

        /// <summary>
        /// Registers new freelancer
        /// </summary>
        /// <param name="newuser"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        /// <response code="500">Freelancer with the given username already exists</response>
        [HttpPost("registerfreelancer")]
        public async Task<IActionResult> registerfreelancer([FromBody] register newuser)
        {
            var userexists = await _context.Profiles
                .Where(p => p.Username == newuser.Username).FirstOrDefaultAsync();

            var category = await _context.Categories
                .Where(c => c.CategoryName == newuser.category).FirstOrDefaultAsync();

            var city = await _context.Cities
                .Where(c => c.CityName == newuser.city).FirstOrDefaultAsync();

            if (userexists != null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError,
                    new { Status = "Error", Message = "Freelancer already exists!" });
            }

            Profiles newprofile = new Profiles
            {
                Username = newuser.Username,
                FirstName = newuser.FirstName,
                LastName = newuser.LastName,
                Password = BCrypt.Net.BCrypt.HashPassword(newuser.Password),
                Email = newuser.Email,
                PhoneNumber = newuser.PhoneNumber,
                UserSince = DateTime.Now.Date,
                CategoryId = category.CategoryId,
                CityId = city.CityId,
                EmailConfirmed = true,
                PhoneNumberConfirmed = true,
                IsActive = false

            };

            var last = _context.Profiles.AsEnumerable().Last();

            newprofile.UserRoles.Add(new UserRoles
            {
                ProfileId = last.ProfileId + 1,
                RoleId = 2,
            });

            _context.Profiles.Add(newprofile);
            _context.SaveChanges();

            return Ok(new { Status = "Success", Message = "Freelancer Successfully registered!" });
        }

        /// <summary>
        /// Registers new employeer
        /// </summary>
        /// <param name="newuser"></param>
        /// <returns></returns>
        /// <response code="200">Success</response>
        /// <response code="500">Employer with the given username already exists</response>
        [HttpPost("registeremployeer")]
        public async Task<IActionResult> registeremployeer([FromBody] register newuser)
        {
            var userexists = await _context.Profiles
                .Where(p => p.Username == newuser.Username).FirstOrDefaultAsync();

            var category = await _context.Categories
                .Where(c => c.CategoryName == newuser.category).FirstOrDefaultAsync();

            var city = await _context.Cities
                .Where(c => c.CityName == newuser.city).FirstOrDefaultAsync();

            if (userexists != null)
            {
                return StatusCode(StatusCodes.Status500InternalServerError,
                    new { Status = "Error", Message = "Employeer already exists!" });
            }

            Profiles newprofile = new Profiles
            {
                Username = newuser.Username,
                FirstName = newuser.FirstName,
                LastName = newuser.LastName,
                Password = BCrypt.Net.BCrypt.HashPassword(newuser.Password),
                Email = newuser.Email,
                PhoneNumber = newuser.PhoneNumber,
                UserSince = DateTime.Now.Date,
                CategoryId = category.CategoryId,
                CityId = city.CityId,
                EmailConfirmed = true,
                PhoneNumberConfirmed = true,
                IsActive = false

            };

            var last = _context.Profiles.AsEnumerable().Last();

            newprofile.UserRoles.Add(new UserRoles
            {
                ProfileId = last.ProfileId + 1,
                RoleId = 3,
            });

            _context.Profiles.Add(newprofile);
            _context.SaveChanges();

            return Ok(new { Status = "Success", Message = "Employeer Successfully registered!" });
        }

        //JWT token generator
        private JwtSecurityToken gettoken(List<Claim> authclaim)
        {
            var authsigningkey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));

            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:ValidIssuer"],
                audience: _configuration["JWT:ValidAudience"],
                expires: DateTime.Now.AddHours(3),
                claims: authclaim,
                signingCredentials: new SigningCredentials(authsigningkey,SecurityAlgorithms.HmacSha256)
                );

            return token;
        }
    }
}
