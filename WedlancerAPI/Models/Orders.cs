using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Orders
    {
        public int OrderId { get; set; }
        public int FreelancerId { get; set; }
        public string PaymentId { get; set; }
        public double Amount { get; set; }
        public int PackageId { get; set; }
        public string Status { get; set; }

        public virtual Profiles Freelancer { get; set; }
        public virtual Packages Package { get; set; }
    }
}
