using Newtonsoft.Json;
using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Profiles
    {
        public Profiles()
        {
            BookingsEmployer = new HashSet<Bookings>();
            BookingsProfile = new HashSet<Bookings>();
            MessagesEmployer = new HashSet<Messages>();
            MessagesFreelancer = new HashSet<Messages>();
            Orders = new HashSet<Orders>();
            Portfolio = new HashSet<Portfolio>();
            ReviewsFreelancer = new HashSet<Reviews>();
            ReviewsProfile = new HashSet<Reviews>();
            UserRoles = new HashSet<UserRoles>();
        }

        public int ProfileId { get; set; }
        public string Username { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public long PhoneNumber { get; set; }
        public int CityId { get; set; }
        public int CategoryId { get; set; }
        public DateTime UserSince { get; set; }
        public string Password { get; set; }
        public bool EmailConfirmed { get; set; }
        public bool PhoneNumberConfirmed { get; set; }
        public bool IsActive { get; set; }
        public string ProfilePicture { get; set; }

        public virtual Categories Category { get; set; }
        public virtual Cities City { get; set; }
        [JsonIgnore]
        public virtual ICollection<Bookings> BookingsEmployer { get; set; }
        [JsonIgnore]
        public virtual ICollection<Bookings> BookingsProfile { get; set; }
        [JsonIgnore]
        public virtual ICollection<Messages> MessagesEmployer { get; set; }
        [JsonIgnore]
        public virtual ICollection<Messages> MessagesFreelancer { get; set; }
        [JsonIgnore]
        public virtual ICollection<Orders> Orders { get; set; }
        [JsonIgnore]
        public virtual ICollection<Portfolio> Portfolio { get; set; }
        [JsonIgnore]
        public virtual ICollection<Reviews> ReviewsFreelancer { get; set; }
        [JsonIgnore]
        public virtual ICollection<Reviews> ReviewsProfile { get; set; }
        [JsonIgnore]
        public virtual ICollection<UserRoles> UserRoles { get; set; }
    }
}
