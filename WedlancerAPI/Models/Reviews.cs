using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Reviews
    {
        public int ReviewId { get; set; }
        public string Message { get; set; }
        public int Stars { get; set; }
        public int FreelancerId { get; set; }
        public int ProfileId { get; set; }

        public virtual Profiles Freelancer { get; set; }
        public virtual Profiles Profile { get; set; }
    }
}
