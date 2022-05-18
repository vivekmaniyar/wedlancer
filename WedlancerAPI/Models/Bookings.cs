using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Bookings
    {
        public int BookingId { get; set; }
        public int ProfileId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string Status { get; set; }
        public int EmployerId { get; set; }

        public virtual Profiles Employer { get; set; }
        public virtual Profiles Profile { get; set; }
    }
}
